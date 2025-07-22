import java.awt.*;
import javax.swing.*;

public class PlagiarizedCalculator {
    private JFrame frame;
    private JTextField display;
    private String currentInput = "0";

    public PlagiarizedCalculator() {
        // Frame setup
        frame = new JFrame("Plagiarized Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 520);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(242, 242, 242));

        // Main panel (calculator)
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setBackground(Color.BLACK);
        calculatorPanel.setLayout(new BoxLayout(calculatorPanel, BoxLayout.Y_AXIS));
        calculatorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display
        display = new JTextField("0");
        display.setEditable(false);
        display.setBackground(new Color(51, 51, 51));
        display.setForeground(Color.WHITE);
        display.setFont(new Font("Arial", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        display.setPreferredSize(new Dimension(280, 80));
        calculatorPanel.add(display);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setBackground(Color.BLACK);

        // Button styles and actions
        String[] buttonLabels = {
            "C", "÷", "×", "−",
            "7", "8", "9", "+",
            "4", "5", "6", "=",
            "1", "2", "3", "",
            "0", ".", "", ""
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);

            if (label.equals("C")) {
                button.setBackground(new Color(153, 153, 153));
                button.setForeground(Color.BLACK);
                button.addActionListener(e -> clearDisplay());
            } else if (label.equals("=")) {
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> calculate());
            } else if (label.equals("÷") || label.equals("×") || label.equals("−") || label.equals("+")) {
                button.setBackground(Color.ORANGE);
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> appendOperator(label));
            } else if (label.equals("0")) {
                button.setBackground(new Color(51, 51, 51));
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> appendNumber(label));
            } else if (label.equals(".")) {
                button.setBackground(new Color(51, 51, 51));
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> appendNumber(label));
            } else if (label.matches("[1-9]")) {
                button.setBackground(new Color(51, 51, 51));
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> appendNumber(label));
            } else {
                button.setEnabled(false);
                button.setBackground(Color.BLACK);
                button.setForeground(Color.BLACK);
            }

            buttonsPanel.add(button);
        }

        calculatorPanel.add(buttonsPanel);
        frame.add(calculatorPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Putanginaaa nyooooooo", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.BOLD, 14));
        footer.setForeground(new Color(51, 51, 51));
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void appendNumber(String number) {
        if (currentInput.equals("0")) {
            currentInput = number;
        } else {
            currentInput += number;
        }
        display.setText(currentInput);
    }

    private void appendOperator(String operator) {
        String lastChar = currentInput.length() > 0 ? currentInput.substring(currentInput.length() - 1) : "";
        if ("+-×÷".contains(lastChar)) {
            currentInput = currentInput.substring(0, currentInput.length() - 1) + operator;
        } else {
            currentInput += operator;
        }
        display.setText(currentInput);
    }

    private void clearDisplay() {
        currentInput = "0";
        display.setText(currentInput);
    }

    private void calculate() {
        try {
            // Replace ÷ and × with / and * for evaluation
            String expr = currentInput.replace("÷", "/").replace("×", "*").replace("−", "-");
            double result = eval(expr);
            currentInput = Double.toString(result);
            display.setText(currentInput);
        } catch (Exception e) {
            currentInput = "Error";
            display.setText(currentInput);
        }
    }

    // Simple expression evaluator (not as robust as JS eval)
    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlagiarizedCalculator());
    }
}
