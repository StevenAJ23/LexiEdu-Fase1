import 'dart:io';

import 'package:google_mlkit_text_recognition/google_mlkit_text_recognition.dart';

class OcrService {
  OcrService()
      : _textRecognizer = TextRecognizer(
          script: TextRecognitionScript.latin,
        );

  final TextRecognizer _textRecognizer;

  /// Devuelve el texto extraído, cadena vacía si no hay texto, o lanza excepción si falla.
  Future<String> extractTextFromImage(String imagePath) async {
    final inputImage = InputImage.fromFile(File(imagePath));
    final recognizedText = await _textRecognizer.processImage(inputImage);

    if (recognizedText.text.trim().isEmpty) return '';

    final buffer = StringBuffer();
    for (final block in recognizedText.blocks) {
      for (final line in block.lines) {
        buffer.writeln(line.text);
      }
      buffer.writeln();
    }
    return buffer.toString().trim();
  }

  void dispose() {
    _textRecognizer.close();
  }
}
