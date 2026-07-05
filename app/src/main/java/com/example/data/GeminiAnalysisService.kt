package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiContent(
  val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
  val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponseFormatText(
  val mimeType: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponseFormat(
  val text: GeminiResponseFormatText? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
  val responseFormat: GeminiResponseFormat? = null,
  val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
  val contents: List<GeminiContent>,
  val generationConfig: GeminiGenerationConfig? = null,
  val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
  val content: GeminiContent
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
  val candidates: List<GeminiCandidate>? = null
)

interface GeminiApiService {
  @POST("v1beta/models/{model}:generateContent")
  suspend fun generateContent(
    @Path("model") model: String,
    @Query("key") apiKey: String,
    @Body request: GeminiGenerateRequest
  ): GeminiGenerateResponse
}

object GeminiAnalysisService {
  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val retrofit = Retrofit.Builder()
    .baseUrl("https://generativelanguage.googleapis.com/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  private val apiService = retrofit.create(GeminiApiService::class.java)

  suspend fun analyzeResume(extractedText: String): GeminiAnalysisResultDto = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    val isKeyEmpty = apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY"
    Log.d("GeminiAnalysis", "API key loaded successfully: ${!isKeyEmpty}")
    if (isKeyEmpty) {
      throw Exception("Missing API key: Please configure your Gemini API Key in the Secrets panel in AI Studio.")
    }

    val textLength = extractedText.length
    Log.d("GeminiAnalysis", "Resume text length: $textLength characters")
    if (extractedText.isBlank()) {
      throw Exception("Resume extraction failure: Extracted resume text is completely empty.")
    }

    val systemPrompt = """
      You are an expert career consultant, senior talent acquisition engineer, and ATS optimization coach. 
      You analyze resume plain text and match it against modern top-tier tech industry standards.
      You MUST respond ONLY with a clean and complete JSON document matching the requested schema exactly.
      Do not include any markdown fences or surrounding texts inside your output outside the JSON payload.
    """.trimIndent()

    val userPrompt = """
      Analyze the following resume text and generate comprehensive career insights.
      
      === RESUME TEXT ===
      $extractedText
      ===================
      
      You must respond with a JSON object that satisfies the following JSON schema exactly:
      {
        "resumeScore": (integer between 0 and 100, reflecting general resume strength),
        "atsScore": (integer between 0 and 100, representing parseability and industry keywords match),
        "technicalScore": (integer between 0 and 100),
        "experienceScore": (integer between 0 and 100),
        "projectScore": (integer between 0 and 100),
        "jobReadiness": (integer between 0 and 100, represent readiness for modern roles),
        "futureEmployability": (integer between 0 and 100, representing future-proofing index over 5 years),
        "currentSkills": [
          {
            "name": "Skill Name",
            "level": "ADVANCED" | "INTERMEDIATE" | "BEGINNER"
          }
        ],
        "missingSkills": [
          {
            "name": "Missing Skill",
            "priority": "HIGH" | "MEDIUM" | "LOW",
            "category": "Cloud" | "Programming" | "Database" | "Tools" | "DevOps",
            "resourcesLink": "URL to resource or roadmap, defaults to https://roadmap.sh"
          }
        ],
        "strengths": [
          "Detailed, impactful strength points (at least 2)"
        ],
        "weaknesses": [
          "Constructive weakness/gap points (at least 1)"
        ],
        "recommendedProjects": [
          {
            "title": "Actionable Project Blueprint Name",
            "description": "Explanatory blueprint of what to build and why for the resume.",
            "skills": ["List of skills learned"],
            "duration": "Estimated completion (e.g. 1-2 weeks or 1 month)",
            "difficulty": "Easy" | "Medium" | "Hard"
          }
        ],
        "roadmap": [
          {
            "week": (week index starting from 1),
            "title": "Milestone Target Title",
            "description": "What they will master and build this week.",
            "status": "COMPLETED" | "IN_PROGRESS" | "LOCKED" (First week in_progress, remaining locked)
          }
        ]
      }
    """.trimIndent()

    val request = GeminiGenerateRequest(
      contents = listOf(
        GeminiContent(parts = listOf(GeminiPart(text = userPrompt)))
      ),
      generationConfig = GeminiGenerationConfig(
        responseFormat = GeminiResponseFormat(
          text = GeminiResponseFormatText(mimeType = "application/json")
        ),
        temperature = 0.2f
      ),
      systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
    )

    var lastException: Exception? = null
    val maxRetries = 3
    val modelName = "gemini-2.5-flash"

    for (attempt in 1..maxRetries) {
      try {
        Log.d("GeminiAnalysis", "Sending Gemini request to model: $modelName (Attempt $attempt of $maxRetries)")
        val response = apiService.generateContent(modelName, apiKey, request)
        Log.d("GeminiAnalysis", "Gemini response success: Response object received")

        val candidates = response.candidates
        if (candidates.isNullOrEmpty()) {
          throw Exception("Gemini API error: Returns an empty content block, possibly due to safety filters or unsupported model/inputs.")
        }
        val textResult = candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
          ?: throw Exception("Gemini API error: No response text returned by active Gemini candidate.")
        
        Log.d("GeminiAnalysis", "Gemini response success: Text output received, length = ${textResult.length} characters")

        // Parse JSON using Moshi
        val cleanedJson = cleanMoshiOutput(textResult)
        val adapter = moshi.adapter(GeminiAnalysisResultDto::class.java)
        val parsed = try {
          adapter.fromJson(cleanedJson)
        } catch (je: Exception) {
          Log.e("GeminiAnalysis", "JSON parsing failure: ${je.localizedMessage}\nRaw text was:\n$textResult")
          throw Exception("JSON parsing failure: Failed to parse structural response from Gemini into career insights schema. Details: ${je.localizedMessage}")
        } ?: throw Exception("JSON parsing failure: Failed to deserialize JSON object payload.")

        Log.d("GeminiAnalysis", "JSON parse success: Successfully converted DTO!")
        return@withContext parsed
      } catch (e: Exception) {
        lastException = e
        Log.w("GeminiAnalysis", "Attempt $attempt failed with exception: ${e.localizedMessage}")
        if (attempt < maxRetries) {
          delay(1000L * attempt) // Exponential backoff retry
        }
      }
    }
    throw lastException ?: Exception("Gemini API error: Analysis failed after maximum retry attempts.")
  }

  private fun cleanMoshiOutput(rawText: String): String {
    var text = rawText.trim()
    // Remove potential markdown code fences if model returned them despite responseFormat restriction
    if (text.startsWith("```json")) {
      text = text.substringAfter("```json").substringBeforeLast("```").trim()
    } else if (text.startsWith("```")) {
      text = text.substringAfter("```").substringBeforeLast("```").trim()
    }
    return text
  }
}
