package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 造作欣的简易二元表达式计算器。
 * 2018年09月14日于S-202。
 * 计算器version：1.0。
 * 哈哈哈哈哈哈哈哈哈哈哈哈哈哈嗝。
 * 难点：二元表达式的合法性判断。
 */


public class Main extends Application
{
    private String expression = "";

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        FlowPane pane = new FlowPane();
        Pane paneResult = new Pane();
        GridPane paneButton = new GridPane();

        // 设置计算器上部分的结果框的高度、宽度、字体大小。
        TextField resultField = new TextField();
        resultField.setPrefHeight(50);
        resultField.setPrefWidth(250);
        resultField.setFont(new Font(20));
        paneResult.getChildren().addAll(resultField);

        // 获取框内的表达式的内容。
        expression = resultField.getText();

        paneButton.setAlignment(Pos.CENTER);
        paneButton.setPadding(new Insets(1, 1, 1, 1));

        Button btSquare = new BigButton("x²");

        // 点击平方按钮“²”的响应事件
        btSquare.setOnAction(e->
                             {
                                 // 获取表达式的值，如果表达式仅为一个数，则点击之后直接将表达式框内的值改为原来数值的平方。
                                 if (isNumber(resultField.getText()))
                                 {
                                     expression = resultField.getText();
                                     double value = Double.parseDouble(expression);
                                     double result = value * value;
                                     resultField.setText(String.valueOf(result));
                                 }

                                 // 如果表达式不是一个数，则不响应点击事件。
                                 else
                                 {
                                     //No operation.
                                 }

                             });

        // 点击倒数按钮的响应事件
        Button btReciprocal = new BigButton("1/x");
        btReciprocal.setOnAction(e->
                                 {
                                     // 获取表达式的值，如果表达式仅为一个数，则点击之后直接将表达式框内的值改为原来数值的倒数。
                                     if (isNumber(resultField.getText()))
                                     {
                                         expression = resultField.getText();
                                         double value = Double.parseDouble(expression);
                                         double result = 1 / value;
                                         resultField.setText(String.valueOf(result));
                                     }

                                     // 如果表达式不是一个数，则不响应点击事件。
                                     else
                                     {
                                         //No operation.
                                     }
                                 });

        // 清除键的点击事件响应，点击“C”，清空框内的所有内容。
        Button btClear = new BigButton("C");
        btClear.setOnAction(e->
                            {
                                resultField.setText("");
                            });

        Button btDivition = new BigButton("÷");

        // 将按钮添加至按钮操作面板。
        paneButton.add(btSquare, 0, 1);
        paneButton.add(btReciprocal, 1, 1);
        paneButton.add(btClear, 2, 1);
        paneButton.add(btDivition, 3, 1);

        Button bt7 = new BigButton("7");
        Button bt8 = new BigButton("8");
        Button bt9 = new BigButton("9");
        Button btMultiplication = new BigButton("×");

        paneButton.add(bt7, 0, 2);
        paneButton.add(bt8, 1, 2);
        paneButton.add(bt9, 2, 2);
        paneButton.add(btMultiplication, 3, 2);

        Button bt4 = new BigButton("4");
        Button bt5 = new BigButton("5");
        Button bt6 = new BigButton("6");
        Button btMinus = new BigButton("-");

        paneButton.add(bt4, 0, 3);
        paneButton.add(bt5, 1, 3);
        paneButton.add(bt6, 2, 3);
        paneButton.add(btMinus, 3, 3);

        Button bt1 = new BigButton("1");
        Button bt2 = new BigButton("2");
        Button bt3 = new BigButton("3");
        Button btPlus = new BigButton("+");

        paneButton.add(bt1, 0, 4);
        paneButton.add(bt2, 1, 4);
        paneButton.add(bt3, 2, 4);
        paneButton.add(btPlus, 3, 4);

        Button bt0 = new BigButton("0");
        Button btPoint = new BigButton(".");
        bt0.setPrefWidth(123.5);
        Button btEqual = new BigButton("=");

        // 按下等于按钮“=”的事件响应。
        btEqual.setOnAction(e->
                            {
                                expression = resultField.getText();

                                // 将表达式从操作符的位置一分为二，存入 operands[]。
                                String[] operands = expression.split("[\\+\\-×÷]");

                                // 如果分离出来的数只有0个或1 个，说明表达式不完全，不能进行操作，即按下等于按钮无响应。
                                if (operands.length < 2)
                                    return;

                                // 读取二元表达式中的操作符。
                                char operator = expression.charAt(operands[0].length());

                                // 读取两个操作数。
                                double number1 = Double.parseDouble(operands[0]);
                                double number2 = Double.parseDouble(operands[1]);

                                double result = 0;

                                // 根据运算符来计算最终的结果
                                switch (operator)
                                {
                                    case '+': result = number1 + number2; break;
                                    case '-': result = number1 - number2; break;
                                    case '×': result = number1 * number2; break;
                                    case '÷': result = number1 / number2; break;
                                }

                                // 将计算结果在运算表达式的框内显示出来。
                                resultField.setText(String.valueOf(result));
                            });

        paneButton.add(bt0, 0, 5);
        paneButton.add(btPoint,2,5);
        paneButton.add(btEqual, 3, 5);
        paneButton.setColumnSpan(bt0, 2);

        // 给数字按钮添加事件响应。
        bt0.setOnAction(e -> addNumber(resultField, bt0));
        bt1.setOnAction(e -> addNumber(resultField, bt1));
        bt2.setOnAction(e -> addNumber(resultField, bt2));
        bt3.setOnAction(e -> addNumber(resultField, bt3));
        bt4.setOnAction(e -> addNumber(resultField, bt4));
        bt5.setOnAction(e -> addNumber(resultField, bt5));
        bt6.setOnAction(e -> addNumber(resultField, bt6));
        bt7.setOnAction(e -> addNumber(resultField, bt7));
        bt8.setOnAction(e -> addNumber(resultField, bt8));
        bt9.setOnAction(e -> addNumber(resultField, bt9));

        // 给运算符按钮添加事件响应。
        btMinus.setOnAction(e -> addOperation(resultField, btMinus));
        btMultiplication.setOnAction(e -> addOperation(resultField, btMultiplication));
        btPlus.setOnAction(e -> addOperation(resultField, btPlus));
        btDivition.setOnAction(e -> addOperation(resultField, btDivition));

        // 给点按钮添加事件响应。
        btPoint.setOnAction(e ->
                            {
                                expression = resultField.getText();

                                // 首先，判断表达式框内是不是只有实数。
                                if (isNumber(expression))
                                {
                                    // 表达式中仅有实数且已经包含了“.”，则按钮按下无响应。
                                    if (expression.contains("."))
                                    {
                                        //No operation.
                                    }
                                    // 否则可以添加“.”。
                                    else
                                    {
                                        addNumber(resultField, btPoint);
                                    }
                                }
                                else
                                {
                                    // 如果表达式是空的，按下点按钮无响应。
                                    if (expression.length() == 0)
                                    {
                                        //No operation.
                                    }
                                    else
                                    {
                                        String[] operands = expression.split("[\\+\\-×÷]");

                                        // 看看运算符后面的数是否合法。
                                        if (!operands[1].contains("."))
                                        {
                                            // 第二个操作数不包含“.”且为空，点击无响应。
                                            if (operands[1].equals(""))
                                            {
                                                //No operation.
                                            }

                                            // 第二个操作数不包含“.”且不为空，则按下点按钮，添加小数点。
                                            else
                                            {
                                                addNumber(resultField, btPoint);
                                            }
                                        }

                                        // 第二个操作数已经包含“.”则点击点按钮无响应。
                                        else
                                        {
                                            //No operation.
                                        }

                                    }

                                }
                            });


        pane.getChildren().addAll(paneResult, paneButton);

        //设置界面的长、宽、标题。
        primaryStage.setTitle("My Calculator");
        primaryStage.setScene(new Scene(pane, 250, 335));
        primaryStage.show();
    }

    /**
     * 添加数字在表达式框内的显示。
     * @param tf 表达式框。
     * @param bt 按下的数字按钮。
     */
    private void addNumber(TextField tf, Button bt)
    {
        // 由于数字的显示是不受限制的，所以不需要判断表达式的合法性，直接响应添加数字即可。
        tf.setText(tf.getText() + bt.getText());
    }

    /**
     * 添加操作符在表达式框内的显示。
     * @param tf 表达式框。
     * @param bt 按下的操作数按钮。
     */
    private void addOperation(TextField tf, Button bt)
    {
        // 如果表达式为空，不显示操作符，操作符按钮按下无响应。
        if (tf.getText().equals(""))
            return;

        // 如果表达式框内已经是一个二元表达式，则不能再增添操作符，操作符按钮按下无响应。
        if (isExpression(tf.getText()))
        {
            //No operation.
        }

        // 如果表达式仅为一个数，为表达式增加按下按钮的操作符。
        else
        {
            tf.setText(tf.getText() + bt.getText());
        }
    }

    /**
     * 判断给定的字符串是否是一个合法的类似于"[operand][operator][operand]"的二元表达式。
     * @param expression 需要判断是否是表达式的字符串。
     * @return 如果给定的字符串是一个合法的二元表达式则返回true，否则返回false。
     * */
    public boolean isExpression(String expression)
    {
        // 这里的pattern是一个正则表达式，表示的是类似于"[operand][operator][operand]"的二元表达式。
        String pattern = "[0-9]*(\\.([0-9]*)?)?[\\+\\-×÷][0-9]*(\\.([0-9]*)?)?";
        return expression.matches(pattern);
    }

    /**
     * 判断给定的字符串是否是一个实数。
     * @param expression 需要判断是否是实数的字符串。
     * @return 如果给定的字符串包含运算符或者为空则返回false，否则返回true。
     */
    public boolean isNumber(String expression)
    {
        // 如果表达式中含有操作符或者表达式为空，则不是实数。
        if (expression.contains("+") || expression.contains("-") || expression.contains("×") || expression.contains("÷") || expression.equals(""))
            return false;
        else
            return true;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

/**
 * 创建一个继承自Button的类BigButton，用于设置按钮的大小和字体，使得所有的按钮样式一致。
 *
 * */
class BigButton extends Button
{
    public BigButton(String text)
    {
        // 创建这个按钮。
        super(text);

        // 设置它的宽度、长度和字号。
        setPrefWidth(61.5);
        setPrefHeight(55);
        setFont(new Font(20));
    }
}