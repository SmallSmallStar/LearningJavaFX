package game2048;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.util.LinkedList;
import java.util.Stack;

import static javafx.scene.paint.Color.*;

public class Main extends Application
{
    private LinePane linePane;
    private Label lbWinOrLose;

    private LinkedList<Label> lableNumber;
    private Label number;

    private int[][] game;

    private Stack<int[][]> lastSteps;

    private Direction direction;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        game = new int[4][4];

        game[0][0] = 2048;


        lastSteps = new Stack<>();

        lableNumber = new LinkedList<Label>();

        linePane = new LinePane();
        linePane.setPadding(new Insets(10,10,10,10));
        linePane.setLayoutX(10);
        linePane.setLayoutY(10);



        linePane.setOnKeyReleased(e->
                                 {
                                     switch (e.getCode())
                                     {
                                         case RIGHT:
                                             direction = Direction.RIGHT;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                                 showNextStep();
                                             }
                                             break;
                                         case DOWN:
                                             direction = Direction.DOWN;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                                 showNextStep();
                                             }
                                             break;
                                         case UP:
                                             direction = Direction.UP;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                                 showNextStep();
                                             }
                                             break;
                                         case LEFT:
                                             direction = Direction.LEFT;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                                 showNextStep();
                                             }
                                             break;
                                         case BACK_SPACE:
                                             showLastStep();
                                             break;
                                     }
                                 });

        primaryStage.setTitle("2048");
        primaryStage.setScene(new Scene(linePane, 300, 300));
        primaryStage.show();

        showNextStep();

        linePane.requestFocus();
    }

    /**
     * 在游戏界面的空白格子内随机生成一个2或者4。
     * @return 返回游戏界面对应的二维数组。
     */
    private void generateNumber()
    {
        int x = 0;
        int y = 0;
        double r = Math.random();
        int number = r < 0.7 ? 2: 4;
        for (;;)
        {
            x = (int) (Math.random() * 4);
            y = (int) (Math.random() * 4);
            if (game[x][y] == 0)
            {
                game[x][y] = number;
                break;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
                System.out.print(game[i][j] + " ");
            System.out.println();
        }
        System.out.println("\n");
    }

    private void showNumber()
    {
        for (Label l : lableNumber)
            linePane.getChildren().remove(l);
        lableNumber.clear();

        for (int x = 0; x < 4; x ++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] != 0)
                {
                    number = new Label();

                    number.layoutXProperty().bind(linePane.widthProperty().divide(4).multiply(y));
                    number.layoutYProperty().bind(linePane.heightProperty().divide(4).multiply(x));

                    number.setPrefWidth(linePane.getWidth() / 4);
                    number.setPrefHeight(linePane.getHeight() / 4);

                    number.setFont(Font.font(30));
                    String text = game[x][y] + "";
                    number.setText(text);

                    lableNumber.addLast(number);
                }
            }
        }
        linePane.getChildren().addAll(lableNumber);
    }

    private void showNextStep()
    {
        if (!isFull())
            generateNumber();
        showNumber();

        int[][] lastStep = new int[4][4];
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                lastStep[i][j] = game[i][j];
            }
        }
        lastSteps.add(lastStep);
    }


    private void showLastStep()
    {
        if (!lastSteps.isEmpty())
        {
            lastSteps.pop();
            game = lastSteps.pop();
            showNumber();
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                System.out.print(game[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }



    private void minus()
    {
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] % 2 != 0)
                    game[x][y] = game[x][y] - 1;
            }
        }
    }

    private void move(Direction direction)
    {
        if (direction.equals(Direction.UP))
        {
            for (int y = 0; y < 4; y++)
            {
                int moveCount = 0;
                if (game[0][y] == 0)
                    moveCount++;
                for (int x = 1; x < 4; x++)
                {
                    if (game[x][y] == 0)
                    {
                        moveCount++;
                        continue;
                    }
                    else if (x - moveCount - 1 >= 0 && game[x][y] == game[x - moveCount - 1][y])
                    {
                        moveCount++;
                        game[x - moveCount][y] = game[x][y] * 2 + 1;
                    }
                    else if (moveCount != 0 && game[x][y] != 0)
                    {
                        game[x - moveCount][y] = game[x][y];
                    }
                    else
                        continue;
                    game[x][y] = 0;
                }
            }
            minus();
            for (int x = 0; x < 4; x++)
            {
                for (int y = 0; y < 4; y++)
                    System.out.print(game[x][y] + " ");
                System.out.println();
            }
        }

        if (direction.equals(Direction.DOWN))
        {
            for (int y = 0; y < 4; y++)
            {
                int moveCount = 0;
                if (game[3][y] == 0)
                    moveCount++;
                for (int x = 2; x >= 0 ; x--)
                {
                    if (game[x][y] == 0)
                    {
                        moveCount++;
                        continue;
                    }
                    else if (x + moveCount + 1 <= 3 && game[x][y] == game[x + moveCount + 1][y])
                    {
                        moveCount++;
                        game[x + moveCount][y] = game[x][y] * 2 + 1;
                    }
                    else if (moveCount != 0 && game[x][y] != 0)
                    {
                        game[x + moveCount][y] = game[x][y];
                    }
                    else
                        continue;
                    game[x][y] = 0;
                }
            }
            minus();
            for (int x = 0; x < 4; x++)
            {
                for (int y = 0; y < 4; y++)
                    System.out.print(game[x][y] + " ");
                System.out.println();
            }
            System.out.println();
        }

        if (direction.equals(Direction.LEFT))
        {
            for (int x = 0; x < 4; x++)
            {
                int moveCount = 0;
                if (game[x][0] == 0)
                    moveCount++;
                for (int y = 1; y < 4; y++)
                {
                    if (game[x][y] == 0)
                    {
                        moveCount++;
                        continue;
                    }
                    else if (y - moveCount - 1 >= 0 && game[x][y] == game[x][y - moveCount - 1])
                    {
                        moveCount++;
                        game[x][y - moveCount] = game[x][y] * 2 + 1;
                    }
                    else if (moveCount != 0 && game[x][y] != 0)
                    {
                        game[x][y - moveCount] = game[x][y];
                    }
                    else
                        continue;
                    game[x][y] = 0;
                }
            }
            minus();
            for (int x = 0; x < 4; x++)
            {
                for (int y = 0; y < 4; y++)
                    System.out.print(game[x][y] + " ");
                System.out.println();
            }
        }

        if (direction.equals(Direction.RIGHT))
        {
            for (int x = 0; x < 4; x++)
            {
                int moveCount = 0;
                if (game[x][3] == 0)
                    moveCount++;
                for (int y = 2; y >= 0; y--)
                {
                    if (game[x][y] == 0)
                    {
                        moveCount++;
                        continue;
                    }
                    else if (y + moveCount + 1 <= 3 && game[x][y] == game[x][y + moveCount + 1])
                    {
                        moveCount++;
                        game[x][y + moveCount] = game[x][y] * 2 + 1;
                    }
                    else if (moveCount != 0 && game[x][y] != 0)
                    {
                        game[x][y + moveCount] = game[x][y];
                    }
                    else
                        continue;
                    game[x][y] = 0;
                }
            }
            minus();
            for (int x = 0; x < 4; x++)
            {
                for (int y = 0; y < 4; y++)
                    System.out.print(game[x][y] + " ");
                System.out.println();
            }
        }
    }

    private boolean win()
    {
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] == 2048)
                    return true;
            }
        }
        return false;
    }

    private boolean isFull()
    {
        for (int x = 0; x <4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] == 0)
                    return false;
            }
        }
        return true;
    }

    private boolean lose()
    {
        if (isFull() == false)
            return false;

        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] == game[x + 1][y])
                    return false;
            }
        }

        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                if (game[x][y] == game[x][y + 1])
                    return false;
            }
        }

        return true;
    }

    private boolean gameOver()
    {
        if (win())
        {
            showWin();
            return true;
        }
        else if (lose())
        {
            showLose();
            return true;
        }
        return false;
    }

    private void showWin()
    {

        lbWinOrLose = new Label("Win!");
        lbWinOrLose.setLayoutX((linePane.getWidth() - lbWinOrLose.getWidth()) / 2);
        lbWinOrLose.setLayoutY((linePane.getHeight() -lbWinOrLose.getHeight())/ 2);
        lbWinOrLose.setFont(Font.font(50));
        linePane.getChildren().addAll(lbWinOrLose);

    }

    private void showLose()
    {
        lbWinOrLose = new Label("Game Over!");
        lbWinOrLose.setLayoutX((linePane.getWidth() - lbWinOrLose.getWidth()) / 2);
        lbWinOrLose.setLayoutY((linePane.getHeight() -lbWinOrLose.getHeight())/ 2);
        lbWinOrLose.setFont(Font.font(50));
        linePane.getChildren().addAll(lbWinOrLose);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    class LinePane extends Pane
    {
        public LinePane()
        {

            // 画横线。
            Line horizontalLine[] = new Line[4];
            for (int i = 1; i < 4; i++)
            {
                horizontalLine[i] = new Line();
                horizontalLine[i].setStartX(0);
                horizontalLine[i].startYProperty().bind(heightProperty().subtract(10).divide(4).multiply(i));
                horizontalLine[i].endXProperty().bind(widthProperty().subtract(10));
                horizontalLine[i].endYProperty().bind(heightProperty().subtract(10).divide(4).multiply(i));
                horizontalLine[i].setStroke(GRAY);
                this.getChildren().add(horizontalLine[i]);
            }

            // 画竖线。
            Line verticalLine[] = new Line[4];
            for (int i = 1; i < 4; i++)
            {
                verticalLine[i] = new Line();
                verticalLine[i].setStartY(0);
                verticalLine[i].startXProperty().bind(widthProperty().subtract(10).divide(4).multiply(i));
                verticalLine[i].endXProperty().bind(widthProperty().subtract(10).divide(4).multiply(i));
                verticalLine[i].endYProperty().bind(heightProperty().subtract(10));
                verticalLine[i].setStroke(GRAY);
                this.getChildren().add(verticalLine[i]);
            }

            // 画边界。
            Line border[] = new Line[4];

            // 上边
            border[0] = new Line(0, 0, 200, 0);
            border[0].endXProperty().bind(widthProperty().subtract(10));
            border[0].setEndY(0);

            // 下边
            border[1] = new Line(0,200,200,200);
            border[1].setStartX(0);
            border[1].startYProperty().bind(heightProperty().subtract(10));
            border[1].endXProperty().bind(widthProperty().subtract(10));
            border[1].endYProperty().bind(heightProperty().subtract(10));

            // 左边
            border[2] = new Line(0,0,0,200);
            border[2].endYProperty().bind(heightProperty().subtract(10));
            border[2].setEndX(0);

            // 右边
            border[3] = new Line(200,0,200,200);
            border[3].startXProperty().bind(widthProperty().subtract(10));
            border[3].setStartY(0);
            border[3].endXProperty().bind(widthProperty().subtract(10));
            border[3].endYProperty().bind(heightProperty().subtract(10));

            for (int i = 0; i < 4; i++)
            {
                border[i].setStroke(BLACK);
                this.getChildren().add(border[i]);
            }
        }
    }
}
