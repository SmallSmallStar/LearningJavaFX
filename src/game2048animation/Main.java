package game2048animation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.paint.Color.*;

public class Main extends Application
{
    // 定义2048游戏界面。
    private LinePane linePane;
    private Label lbWinOrLose;

    // 定义每次显示的数字标签。
    private LinkedList<Label> labelNumber;
    private Label number;

    // 定义后台的游戏数据结构即二维数组存储游戏的操作数。
    private int[][] game;

    // 定义存储每次移动之后的状态。
    private Stack<int[][]> lastSteps;

    // 定义移动的方向。
    private Direction direction;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // 初始化一个4*4的数组存放游戏结果。
        game = new int[4][4];

        // 初始化存储步骤的栈。
        lastSteps = new Stack<>();

        labelNumber = new LinkedList<Label>();

        linePane = new LinePane();
        linePane.setPadding(new Insets(10,10,10,10));
        linePane.setLayoutX(10);
        linePane.setLayoutY(10);

        // 设置游戏的键盘控制。
        linePane.setOnKeyReleased(e->
                                 {
                                     switch (e.getCode())
                                     {
                                         case RIGHT:
                                             direction = Direction.RIGHT;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                             }
                                             break;
                                         case DOWN:
                                             direction = Direction.DOWN;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                             }
                                             break;
                                         case UP:
                                             direction = Direction.UP;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                             }
                                             break;
                                         case LEFT:
                                             direction = Direction.LEFT;
                                             if (gameOver() == false)
                                             {
                                                 move(direction);
                                             }
                                             break;
                                         case Q:
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
     * 格子向左移动。
     */
    private void moveLeft()
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

                Label lblToMove = null;
                for (Label l : labelNumber)
                {
                    if (l.getLayoutX() == y * l.getWidth() && l.getLayoutY() == x * l.getHeight())
                    {
                        lblToMove = l;
                        break;
                    }
                }
                if (lblToMove != null)
                    moveLeft(lblToMove, moveCount);
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

    /**
     * 格子向左移动的动画效果。
     * @param lblToMove 需要移动的标签。
     * @param moveCount 移动的步数。
     */
    private void moveLeft(Label lblToMove, int moveCount)
    {
        int frameCount = 30;
        AtomicInteger i = new AtomicInteger(0);

        EventHandler<ActionEvent> handler = e ->
        {
            lblToMove.setLayoutX(lblToMove.getLayoutX() - (moveCount * lblToMove.getWidth()) / frameCount);
            i.getAndIncrement();
            if (i.get() >= frameCount)
                showNumber();
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16), handler));
        animation.setAutoReverse(false);
        animation.setCycleCount(frameCount);
        animation.play();
    }

    /**
     * 格子向右移动。
     */
    private void moveRight()
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

                Label lblToMove = null;
                for (Label l : labelNumber)
                {
                    if (l.getLayoutX() == y * l.getWidth() && l.getLayoutY() == x * l.getHeight())
                    {
                        lblToMove = l;
                        break;
                    }
                }
                if (lblToMove != null)
                    moveRight(lblToMove, moveCount);
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

    /**
     * 格子向右移动的动画效果。
     * @param lblToMove 需要移动的标签。
     * @param moveCount 移动的步数。
     */
    private void moveRight(Label lblToMove, int moveCount)
    {
        int frameCount = 30;
        AtomicInteger i = new AtomicInteger(0);

        EventHandler<ActionEvent> handler = e ->
        {
            lblToMove.setLayoutX(lblToMove.getLayoutX() + (moveCount * lblToMove.getWidth()) / frameCount);
            i.getAndIncrement();
            if (i.get() >= frameCount)
                showNumber();
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16), handler));
        animation.setAutoReverse(false);
        animation.setCycleCount(frameCount);
        animation.play();
    }

    /**
     * 格子向上移动。
     */
    private void moveUp()
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

                Label lblToMove = null;
                for (Label l : labelNumber)
                {
                    if (l.getLayoutX() == y * l.getWidth() && l.getLayoutY() == x * l.getHeight())
                    {
                        lblToMove = l;
                        break;
                    }
                }
                if (lblToMove != null)
                    moveUp(lblToMove, moveCount);
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

    /**
     * 格子向上移动的动画效果。
     * @param lblToMove 需要移动的标签。
     * @param moveCount 移动的步数。
     */
    private void moveUp(Label lblToMove, int moveCount)
    {
        int frameCount = 30;
        AtomicInteger i = new AtomicInteger(0);

        EventHandler<ActionEvent> handler = e ->
        {
            lblToMove.setLayoutY(lblToMove.getLayoutY() - (moveCount * lblToMove.getHeight()) / frameCount);
            i.getAndIncrement();
            if (i.get() >= frameCount)
                showNumber();
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16), handler));
        animation.setAutoReverse(false);
        animation.setCycleCount(frameCount);
        animation.play();
    }

    /**
     * 格子向下移动。
     */
    private void moveDown()
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

                Label lblToMove = null;
                for (Label l : labelNumber)
                {
                    if (l.getLayoutX() == y * l.getWidth() && l.getLayoutY() == x * l.getHeight())
                    {
                        lblToMove = l;
                        break;
                    }
                }
                if (lblToMove != null)
                    moveDown(lblToMove, moveCount);
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

    /**
     * 格子向下移动的动画效果。
     * @param lblToMove 需要移动的标签。
     * @param moveCount 移动的步数。
     */
    private void moveDown(Label lblToMove, int moveCount)
    {
        int frameCount = 30;
        AtomicInteger i = new AtomicInteger(0);

        EventHandler<ActionEvent> handler = e ->
        {
            lblToMove.setLayoutY(lblToMove.getLayoutY() + (moveCount * lblToMove.getHeight()) / frameCount);
            i.getAndIncrement();
            if (i.get() >= frameCount)
                showNumber();
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16), handler));
        animation.setAutoReverse(false);
        animation.setCycleCount(frameCount);
        animation.play();
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

        // 70%的可能性生成2,30%的可能性生成4。
        int number = r < 0.7 ? 2: 4;

        // 循环找到一个空位置存放随机生成的2或者4。
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

        // 在控制台输出当前状态。
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
                System.out.print(game[i][j] + " ");
            System.out.println();
        }
        System.out.println("\n");
    }

    /**
     * 在游戏界面显示当前的游戏状态。
     */
    private void showNumber()
    {
        // 将游戏界面上的标签依次删除。
        for (Label l : labelNumber)
            linePane.getChildren().remove(l);

        // 清空数字标签链表。
        labelNumber.clear();

        for (int x = 0; x < 4; x ++)
        {
            for (int y = 0; y < 4; y++)
            {
                // 只要不是空白格子，将格子里面的数字显示在游戏界面上。
                if (game[x][y] != 0)
                {
                    // 定义一个新的label来存放界面对应位置的新状态。
                    number = new Label();

                    // 绑定label与边界的相应位置。
//                    number.layoutXProperty().bind(linePane.widthProperty().divide(4).multiply(y));
//                    number.layoutYProperty().bind(linePane.heightProperty().divide(4).multiply(x));

                    number.setLayoutX(linePane.getWidth() / 4 * y);
                    number.setLayoutY(linePane.getHeight() / 4 * x);

                    // 设置宽和高。
                    number.setPrefWidth(linePane.getWidth() / 4);
                    number.setPrefHeight(linePane.getHeight() / 4);

                    // 设置label的内容。
                    number.setFont(Font.font(30));
                    String text = game[x][y] + "";
                    number.setText(text);

                    // 将显示数字的标签加入到链表里面。
                    labelNumber.addLast(number);
                }
            }
        }

        // 将数字标签添加到界面中。
        linePane.getChildren().addAll(labelNumber);

        // 将最后一步的状态加入到状态栈中，以便撤销操作。
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

    /**
     * 显示下一步的状态。
     */
    private void showNextStep()
    {
        // 如果格子没有被填满，在空白的格子里面填上2或4。
        if (!isFull())
            generateNumber();

        // 在界面中将游戏状态显示出来。
        showNumber();

        // 将最后一步的状态加入到状态栈中，以便撤销操作。
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

    /**
     * 显示上一步状态。
     */
    private void showLastStep()
    {
        // 只要状态栈里面有状态，就让前一步状态出栈。
        if (!lastSteps.isEmpty())
        {
            lastSteps.pop();
            game = lastSteps.pop();
            showNumber();
        }

        // 在控制台显示出当前状态（此时就是撤销后的状态）。
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

    /**
     * 将奇数的数字减去1，用于move，因为在move里面，如果相邻的两位可以相加，就乘2加1，变成了奇数。
     * 为什么move里面要乘2加1：由于每次移动一个方向只能移动一下，比如0 2 2 4，向右移动得到0 0 4 4，而不是0 0 0 8。*2+1可以解决此类问题。
     */
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

    /**
     * 移动方块，移动方向上如果相邻位置数字相同的话，就加起来。
     * @param direction 移动方向。
     */
    private void move(Direction direction)
    {
        // 向上移动。
        if (direction.equals(Direction.UP))
        {
            moveUp();
        }

        // 向下移动。
        if (direction.equals(Direction.DOWN))
        {
            moveDown();
        }

        // 向左移动。
        if (direction.equals(Direction.LEFT))
        {
            moveLeft();
        }

        // 向右移动。
        if (direction.equals(Direction.RIGHT))
        {
            moveRight();
        }

        generateNumber();
    }

    /**
     * 判断游戏是不是赢了。
     * @return 如果有格子里面的数字为2048，则赢了，返回true，否则返回false。
     */
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

    /**
     * 判断格子有没有满，用于判断游戏是否结束。
     * @return
     */
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

    /**
     * 判断游戏是不是结束了。
     * @return 如果格子还没有满，没有结束，返回false，或者格子满了，但是有相邻格子数字相同的情况，则也没有结束，返回false，否则游戏结束，返回true。
     */
    private boolean lose()
    {
        // 格子没有满。
        if (isFull() == false)
            return false;

        // 横向中相邻的格子有数字相同。
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (game[x][y] == game[x + 1][y])
                    return false;
            }
        }

        // 纵向中相邻的格子有数字相同。
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

    /**
     * 游戏结束只有两种情况：游戏输了，游戏赢了，返回true，否则返回false。
     * @return
     */
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

    /**
     * 在界面上显示游戏赢了。
     */
    private void showWin()
    {

        lbWinOrLose = new Label("Win!");
        lbWinOrLose.setLayoutX((linePane.getWidth() - lbWinOrLose.getWidth()) / 2);
        lbWinOrLose.setLayoutY((linePane.getHeight() -lbWinOrLose.getHeight())/ 2);
        lbWinOrLose.setFont(Font.font(50));
        linePane.getChildren().addAll(lbWinOrLose);

    }

    /**
     * 在界面上显示游戏输了。
     */
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
