package com.example.data

import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.util.zip.ZipInputStream

object ResumeParserService {

  fun parseResume(context: Context, uri: Uri, fileName: String): String {
    val inputStream = context.contentResolver.openInputStream(uri) 
      ?: throw IllegalArgumentException("Failed to open file: $fileName")
    
    val extractedText = try {
      when {
        fileName.endsWith(".docx", ignoreCase = true) -> {
          parseDocx(inputStream)
        }
        fileName.endsWith(".pdf", ignoreCase = true) -> {
          parsePdf(inputStream)
        }
        else -> {
          // Fallback or plain text file
          inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        }
      }
    } catch (e: Exception) {
      throw Exception("Error extracting text from $fileName: ${e.localizedMessage}")
    } finally {
      try { inputStream.close() } catch (ignored: Exception) {}
    }

    val cleanedText = cleanText(extractedText)
    if (cleanedText.isBlank()) {
      throw Exception("Resume text extraction resulted in empty content. Please verify that the document contains readable text.")
    }
    return cleanedText
  }

  private fun parseDocx(inputStream: InputStream): String {
    val zipStream = ZipInputStream(inputStream)
    var entry = zipStream.nextEntry
    var documentXml = ""
    while (entry != null) {
      if (entry.name == "word/document.xml") {
        documentXml = zipStream.bufferedReader(Charsets.UTF_8).readText()
        break
      }
      entry = zipStream.nextEntry
    }
    zipStream.close()

    if (documentXml.isEmpty()) {
      throw Exception("Invalid DOCX format: word/document.xml not found.")
    }

    // Extract text from w:t elements
    val regex = Regex("<w:t[^>]*>(.*?)</w:t>")
    val matches = regex.findAll(documentXml)
    val sb = java.lang.StringBuilder()
    for (match in matches) {
      val rawText = match.groupValues[1]
      // Unescape basic XML entities
      val unescapedText = rawText
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
      sb.append(unescapedText).append(" ")
    }
    return sb.toString()
  }

  private fun parsePdf(inputStream: InputStream): String {
    // Elegant, custom PDF stream text scanning
    val bytes = inputStream.readBytes()
    val sb = java.lang.StringBuilder()
    var i = 0
    val size = bytes.size
    
    while (i < size) {
      if (bytes[i] == '('.code.toByte()) {
        i++
        val textBytes = java.io.ByteArrayOutputStream()
        var escaped = false
        var depth = 1
        
        while (i < size && depth > 0) {
          val b = bytes[i]
          if (escaped) {
            textBytes.write(b.toInt())
            escaped = false
          } else if (b == '\\'.code.toByte()) {
            escaped = true
          } else if (b == '('.code.toByte()) {
            depth++
            textBytes.write(b.toInt())
          } else if (b == ')'.code.toByte()) {
            depth--
            if (depth > 0) {
              textBytes.write(b.toInt())
            }
          } else {
            textBytes.write(b.toInt())
          }
          i++
        }
        
        val stringContent = textBytes.toString("UTF-8")
        // Filter out non-printable ASCII or system strings where possible
        val printable = stringContent.filter { it.code in 32..126 || it.code in 9..13 || it.code in 160..255 }
        if (printable.length > 1 && !printable.startsWith("/") && !printable.contains("Identity") && !printable.contains("Font")) {
          sb.append(printable).append(" ")
        }
      } else {
        i++
      }
    }
    
    val text = sb.toString()
    if (text.isNotBlank()) {
      return text
    }
    
    // De-duplicating / fallback: if we cannot find any parenthesis-based text blocks,
    // let's scan for contiguous sequences of printable ASCII to at least get human readable words.
    val fallbackBuilder = java.lang.StringBuilder()
    var wordBuffer = java.lang.StringBuilder()
    for (b in bytes) {
      val c = b.toInt().toChar()
      if (c.code in 32..126 || c == '\n' || c == '\t' || c == '\r') {
        wordBuffer.append(c)
      } else {
        if (wordBuffer.length >= 4) {
          // Avoid PDF structural garbage
          val word = wordBuffer.toString().trim()
          if (!word.startsWith("/") && !word.contains("xref") && !word.contains("trailer") && !word.contains("obj") && !word.contains("stream")) {
            fallbackBuilder.append(word).append(" ")
          }
        }
        wordBuffer = java.lang.StringBuilder()
      }
    }
    return fallbackBuilder.toString()
  }

  private fun cleanText(text: String): String {
    return text
      .replace(Regex("[\\r\\n\\t]+"), " ")
      .replace(Regex("\\s+"), " ")
      .trim()
  }
}
