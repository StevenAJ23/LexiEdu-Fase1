import 'package:flutter_test/flutter_test.dart';

import 'package:incluapp/main.dart';

void main() {
  testWidgets('LexiEdu muestra la pantalla de bienvenida', (tester) async {
    await tester.pumpWidget(const LexiEduApp());

    expect(find.text('LexiEdu'), findsWidgets);
  });
}
