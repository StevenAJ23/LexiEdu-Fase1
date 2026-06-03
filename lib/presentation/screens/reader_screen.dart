import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../core/theme/app_theme.dart';
import '../../core/utils/app_snackbar.dart';
import '../../data/services/tts_service.dart';

class ReaderScreen extends StatefulWidget {
  const ReaderScreen({
    required this.extractedText,
    required this.imagePath,
    required this.processingMs,
    super.key,
  });

  final String extractedText;
  final String imagePath;
  final int processingMs;

  @override
  State<ReaderScreen> createState() => _ReaderScreenState();
}

class _ReaderScreenState extends State<ReaderScreen> {
  final TtsService _ttsService = TtsService();
  double _speechRate = 0.45;
  bool _isSpeaking = false;

  bool get _hasText => widget.extractedText.trim().isNotEmpty;

  int get _wordCount =>
      widget.extractedText.trim().isEmpty ? 0 : widget.extractedText.trim().split(RegExp(r'\s+')).length;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      await _speak(showFeedback: false);
    });
  }

  @override
  void dispose() {
    _ttsService.dispose();
    super.dispose();
  }

  Future<void> _speak({bool showFeedback = true}) async {
    if (!_hasText) {
      if (mounted) {
        AppSnackBar.showWarning(
          context,
          'No hay texto para leer. Vuelve atrás y captura una imagen con texto visible.',
        );
      }
      return;
    }

    await HapticFeedback.selectionClick();
    final started = await _ttsService.speak(widget.extractedText);

    if (!mounted) return;

    if (!started) {
      AppSnackBar.showError(
        context,
        'No se pudo iniciar la lectura. Comprueba el volumen del dispositivo.',
      );
      return;
    }

    setState(() {
      _isSpeaking = true;
    });

    if (showFeedback) {
      AppSnackBar.showSuccess(context, 'Lectura iniciada');
    }
  }

  Future<void> _pause() async {
    await HapticFeedback.selectionClick();
    await _ttsService.pause();

    if (!mounted) return;

    setState(() {
      _isSpeaking = false;
    });
  }

  Future<void> _stop() async {
    await HapticFeedback.mediumImpact();
    await _ttsService.stop();

    if (!mounted) return;

    setState(() {
      _isSpeaking = false;
    });
  }

  Future<void> _changeSpeechRate(double value) async {
    setState(() {
      _speechRate = value;
    });
    await _ttsService.setSpeechRate(value);
  }

  Future<void> _copyText() async {
    await HapticFeedback.selectionClick();
    await Clipboard.setData(ClipboardData(text: widget.extractedText));
    if (!mounted) return;
    AppSnackBar.showSuccess(context, 'Texto copiado al portapapeles');
  }

  String _speedLabel(double value) {
    if (value <= 0.30) return 'Muy lenta';
    if (value <= 0.45) return 'Lenta';
    if (value <= 0.60) return 'Normal';
    return 'Rápida';
  }

  @override
  Widget build(BuildContext context) {
    final seconds = (widget.processingMs / 1000).toStringAsFixed(1);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Lectura'),
        actions: [
          if (_hasText)
            IconButton(
              onPressed: _copyText,
              icon: const Icon(Icons.copy_rounded),
              tooltip: 'Copiar texto',
            ),
        ],
      ),
      body: SafeArea(
        child: Padding(
          padding: AppTheme.screenPadding,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                'Resultado OCR',
                semanticsLabel: 'Resultado del reconocimiento de texto',
                style: Theme.of(context).textTheme.headlineMedium,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 8),
              _StatsRow(seconds: seconds, wordCount: _wordCount),
              const SizedBox(height: AppTheme.elementSpacing),
              Expanded(
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(18),
                  decoration: BoxDecoration(
                    color: AppTheme.surface,
                    border: Border.all(color: AppTheme.accentWhite, width: 2),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: SingleChildScrollView(
                    child: _hasText
                        ? SelectableText(
                            widget.extractedText,
                            style: Theme.of(context).textTheme.bodyLarge,
                          )
                        : Text(
                            'No se detectó texto en la imagen.',
                            style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                                  color: AppTheme.disabledGray,
                                  fontStyle: FontStyle.italic,
                                ),
                            textAlign: TextAlign.center,
                          ),
                  ),
                ),
              ),
              const SizedBox(height: 18),
              Row(
                children: [
                  ExcludeSemantics(
                    child: const Icon(
                      Icons.speed_rounded,
                      color: AppTheme.primaryYellow,
                      size: 22,
                    ),
                  ),
                  const SizedBox(width: 8),
                  Text(
                    'Velocidad',
                    semanticsLabel: 'Control de velocidad de lectura',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const Spacer(),
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                    decoration: BoxDecoration(
                      color: AppTheme.primaryYellow.withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(20),
                      border: Border.all(
                        color: AppTheme.primaryYellow.withValues(alpha: 0.4),
                      ),
                    ),
                    child: Text(
                      _speedLabel(_speechRate),
                      style: const TextStyle(
                        color: AppTheme.primaryYellow,
                        fontWeight: FontWeight.w700,
                        fontSize: 15,
                      ),
                    ),
                  ),
                ],
              ),
              Slider(
                value: _speechRate,
                min: 0.25,
                max: 0.8,
                divisions: 11,
                label: _speedLabel(_speechRate),
                semanticFormatterCallback: _speedLabel,
                onChanged: _changeSpeechRate,
              ),
              const SizedBox(height: 12),
              ElevatedButton.icon(
                onPressed: () => _speak(showFeedback: true),
                icon: Icon(
                  _isSpeaking ? Icons.replay : Icons.volume_up,
                  semanticLabel: '',
                ),
                label: Text(_isSpeaking ? 'Repetir lectura' : 'Leer texto'),
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: OutlinedButton.icon(
                      onPressed: _pause,
                      icon: const Icon(Icons.pause, semanticLabel: ''),
                      label: const Text('Pausar'),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: OutlinedButton.icon(
                      onPressed: _stop,
                      icon: const Icon(Icons.stop, semanticLabel: ''),
                      label: const Text('Detener'),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ── Fila de estadísticas ──────────────────────────────────────────────────────

class _StatsRow extends StatelessWidget {
  const _StatsRow({required this.seconds, required this.wordCount});

  final String seconds;
  final int wordCount;

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        _StatChip(
          icon: Icons.timer_outlined,
          label: '$seconds s',
          semantics: 'Procesado en $seconds segundos',
        ),
        const SizedBox(width: 12),
        _StatChip(
          icon: Icons.text_fields_rounded,
          label: '$wordCount palabras',
          semantics: '$wordCount palabras detectadas',
        ),
      ],
    );
  }
}

class _StatChip extends StatelessWidget {
  const _StatChip({
    required this.icon,
    required this.label,
    required this.semantics,
  });

  final IconData icon;
  final String label;
  final String semantics;

  @override
  Widget build(BuildContext context) {
    return Semantics(
      label: semantics,
      excludeSemantics: true,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: BoxDecoration(
          color: AppTheme.surface,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: AppTheme.primaryYellow.withValues(alpha: 0.30),
          ),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, color: AppTheme.primaryYellow, size: 16),
            const SizedBox(width: 6),
            Text(
              label,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: AppTheme.disabledGray,
                    fontSize: 15,
                  ),
            ),
          ],
        ),
      ),
    );
  }
}
