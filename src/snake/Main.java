package snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.LinkedList;

import static javafx.scene.paint.Color.*;

/**
 * 小欣欣的造作蛇。
 * 2018年09月18日于S-202。
 * 造作蛇version：1.0。
 * 哈哈哈哈哈哈哈哈哈哈哈哈哈哈嗝。
 * 难点：小蛇的移动、吃苹果操作、判断游戏结束（小蛇撞墙或吃了自己）。
 */
public class Main extends Application {

    private LinkedList<Rectangle> snake;
    private Direction direction = Direction.UP;
    private int score;
    private Rectangle apple;
    private LinePane linePane;
    private Label lose;
    private Label lbScore;
    private Pane pane;
    private Animation animation;

    /**
     * 初始化一条小蛇，并将小蛇在界面中显示出来。
     * 定义小蛇的移动初始方向为向右。
     */
    private void initialize()
    {
        snake = new LinkedList<>();
        snake.addLast(new Rectangle(30,30, 15, 15));
        snake.addLast(new Rectangle(30,45, 15, 15));
        snake.addLast(new Rectangle(30,60, 15, 15));
        snake.addLast(new Rectangle(30,75, 15, 15));
        snake.addLast(new Rectangle(30,90, 15, 15));

        for (Rectangle r : snake)
            r.setFill(GREEN);

        snake.getFirst().setFill(BLACK);

        score = 0;
        direction = Direction.RIGHT;

        if (pane != null && pane.getChildren().contains(lose))
            pane.getChildren().remove(lose);
    }

    /**
     * 随机生成一个苹果，并在界面中显示出来。
     * @return 随机生成的苹果。
     */
    private Rectangle showApple()
    {
        int x = (int)(Math.random() * 20);
        int y = (int)(Math.random() * 20);
        Rectangle apple = new Rectangle(x * 15, y * 15, 15,15);
        for (Rectangle rectangle : snake)
        {
            if ((apple.getX() == rectangle.getX()) && (apple.getY() == rectangle.getY()))
                apple = showApple();
        }
        apple.setFill(RED);
        return apple;
    }

    /**
     * 获取小蛇的移动方向。
     * @return 移动之后的蛇头。
     */
    private Rectangle getNextHead()
    {
        // 获取蛇头。
        Rectangle head = snake.getFirst();
        Rectangle r = null;

        // 根据蛇的移动方向，生成新的蛇头。
        switch (direction)
        {
            case UP:
                r = new Rectangle(head.getX(), head.getY() - 15, head.getWidth(), head.getHeight());
                break;
            case DOWN:
                r = new Rectangle(head.getX(), head.getY() + 15, head.getWidth(), head.getHeight());
                break;
            case LEFT:
                r = new Rectangle(head.getX() - 15, head.getY(), head.getWidth(), head.getHeight());
                break;
            case RIGHT:
                r = new Rectangle(head.getX() + 15, head.getY(), head.getWidth(), head.getHeight());
                break;
        }
        return r;
    }

    /**
     * 判断小蛇是否迟到了苹果。
     * @param head 获取到蛇头的方块。
     * @param apple 苹果的方块。
     * @return 如果吃到了苹果则返回true，否则返回false。
     */
    private boolean eatApple(Rectangle head, Rectangle apple)
    {
        // 判断蛇头的方块的横纵坐标是否和苹果的相一致。
        if ((head.getX() == apple.getX()) && (head.getY() == apple.getY()))
            return true;
        else
            return false;
    }

    /**
     * 当小蛇迟到苹果后，修改小蛇的状态。
     * @param apple 苹果的方块。
     */
    private void eatApple(Rectangle apple)
    {
        // 将苹果涂成黑色作为新的蛇头。
        apple.setFill(BLACK);

        // 将新的蛇头加入到蛇中。
        snake.addFirst(apple);

        // 提高小蛇的运动速度。
        animation.setRate(1 + score / 100);
    }

    /**
     * 当小蛇吃到苹果之后，修改得分。
     * @param lbscore 显示的得分的标签。
     */
    private void setScore(Label lbscore)
    {
        // 获取当前的得分。
        int score = Integer.parseInt(lbscore.getText());

        // 加分。
        score += 10;

        // 将新的得分显示出来。
        lbscore.setText(String.valueOf(score));
    }

    /**
     * 在吃掉苹果之后，再随机生成一个新的苹果。
     */
    private void generateNextApple()
    {
        apple = showApple();
        linePane.getChildren().add(apple);
    }

    /**
     * 移动小蛇。
     * @param newHead 获取小蛇的新的头的方块。
     * @param lbScore 显示得分的标签。
     */
    private void move(Rectangle newHead, Label lbScore)
    {
        // 如果小蛇不动，则不对小蛇进行移动操作。
        if (animation.getStatus() == Animation.Status.PAUSED)
            return;

        // 如果吃到了苹果，让蛇进行吃苹果的移动操作。
        if (eatApple(newHead,apple))
        {
            eatApple(apple);
            setScore(lbScore);
            generateNextApple();
        }
        // 否则，小蛇就仅为单纯的移动。
        else
        {
            // 将移动的新的蛇头涂成黑色。
            newHead.setFill(BLACK);

            // 加入新的头，并在界面中显示出来。
            snake.addFirst(newHead);
            linePane.getChildren().add(newHead);

            // 将原先的蛇尾从蛇中删除，并在界面中删除蛇尾。
            Rectangle tail = snake.removeLast();
            linePane.getChildren().remove(tail);
        }
    }

    /**
     * 判断小蛇是否撞到了墙。
     * @param nextHead 获取到蛇的运动的下一个新的头。
     * @return 如果撞到了墙，返回true，否则返回false。
     */
    private boolean touchWall(Rectangle nextHead)
    {
        // 判断新蛇头的坐标是不是在墙外面了。
        if (nextHead.getX() == -15 || nextHead.getY() == -15 || nextHead.getX() == 300 || nextHead.getY() == 300)
            return true;
        else
            return false;
    }

    /**
     * 判断小蛇是否咬到了自己。
     * @param nextHead 获取到的蛇运动的下一个新的头。
     * @return 如果咬到了自己，返回true，否则返回false。
     */
    private boolean eatItself(Rectangle nextHead)
    {
        // 遍历一下整条蛇，看看蛇运动的新头是不是在自己的身上。
        for (Rectangle rectangle : snake)
        {
            // 注意，蛇头肯定是在自己的身上的。
            if ((rectangle == snake.getFirst()))
                continue;

            // 咬到自己，返回true。
            if ((nextHead.getX() == rectangle.getX()) && (nextHead.getY() == rectangle.getY()))
                return true;
        }
        return false;
    }

    /**
     * 判断游戏是不是玩输了。
     * @param nextHead 获取到的蛇运动的下一个新的头。
     * @return 如果撞到了墙或者咬到了自己，返回true，否则返回false。
     */
    private boolean lose(Rectangle nextHead)
    {
        if (touchWall(nextHead) || eatItself(nextHead))
            return true;
        else
            return false;
    }

    /**
     * 显示游戏失败提示。
     * @param nextHead 获取到的蛇运动的下一个新的头。
     */
    private void showLoseMeeage(Rectangle nextHead)
    {
        if (lose(nextHead))
        {
            // 只要pane里面有了lose，说明游戏已经输了。
            if (pane.getChildren().contains(lose))
                return;

            // 否则，如果游戏失败，将lose添加至pane，并显示文字。
            lose.setText("Game Over!");
            lose.setFont(new Font(60));
            pane.getChildren().add(lose);

            // 小蛇停止运动。
            animation.stop();
        }
    }

    /**
     * 小蛇向上运动。
     */
    private void turnUp()
    {
        // 获取小蛇当前的头。
        Rectangle head = snake.getFirst();

        // 获取小蛇按照当前方向移动的新头。
        Rectangle newHead = getNextHead();

        // 如果输了，提示Game Over。
        if (lose(newHead))
        {
            showLoseMeeage(newHead);
        }
        else
        {
            // 若当前运动为向上，则向下的操作无响应
            if (!direction.equals(Direction.DOWN))
            {
                // 移动小蛇。
                head.setFill(GREEN);
                move(newHead, lbScore);
                direction = Direction.UP;
            }
        }
    }

    /**
     * 小蛇向下运动。
     */
    private void turnDown()
    {
        Rectangle head = snake.getFirst();
        Rectangle newHead = getNextHead();

        if (lose(newHead))
        {
            showLoseMeeage(newHead);
        }
        else
        {
            if (!direction.equals(Direction.UP))
            {
                head.setFill(GREEN);
                move(newHead, lbScore);
                direction = Direction.DOWN;
            }
        }
    }

    /**
     * 小蛇向左运动。
     */
    private void turnLeft()
    {
        Rectangle head = snake.getFirst();
        Rectangle newHead = getNextHead();

        if (lose(newHead))
        {
            showLoseMeeage(newHead);
        }
        else
        {
            if (!direction.equals(Direction.RIGHT))
            {
                head.setFill(GREEN);
                move(newHead, lbScore);
                direction = Direction.LEFT;
            }
        }
    }

    /**
     * 小蛇向右运动。
     */
    private void turnRight()
    {
        Rectangle head = snake.getFirst();
        Rectangle newHead = getNextHead();

        if (lose(newHead))
        {
            showLoseMeeage(newHead);
        }
        else
        {
            if (!direction.equals(Direction.LEFT))
            {
                head.setFill(GREEN);
                move(newHead, lbScore);
                direction = Direction.RIGHT;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // 初始化小蛇。
        initialize();

        pane = new Pane();

        // 绘制网格，即小蛇运动界面。
        linePane = new LinePane();
        linePane.setPadding(new Insets(10,10,10,10));
        linePane.setLayoutX(10);
        linePane.setLayoutY(10);

        // 将小蛇显示出来。
        for (Rectangle r : snake)
            linePane.getChildren().add(r);

        // 随机生成一个苹果并显示出来。
        apple = showApple();
        linePane.getChildren().add(apple);

        // 绘制控制小蛇运动的按钮。
        GridPane buttonPane = new GridPane();
        buttonPane.setLayoutX(350);
        buttonPane.setLayoutY(100);
        buttonPane.setVgap(10);
        buttonPane.setHgap(10);
        Button btUp = new BigButton("Up");
        Button btDown = new BigButton("Down");
        Button btRight = new BigButton("Right");
        Button btLeft = new BigButton("Left");
        Button btPause = new BigButton("Pause");
        buttonPane.add(btUp,1,0);
        buttonPane.add(btDown,1,2);
        buttonPane.add(btLeft,0,1);
        buttonPane.add(btRight,2,1);
        buttonPane.add(btPause,1,1);

        Button btRestart = new Button("Restart");
        pane.getChildren().add(btRestart);
        btRestart.setLayoutX(490);
        btRestart.setLayoutY(280);

        // 绘制显示得分情况的标签。
        Pane scorePane = new Pane();
        scorePane.setLayoutX(350);
        scorePane.setLayoutY(10);
        Label label = new Label("SCORE:");
        label.setFont(new Font(30));
        lbScore = new Label();
        lbScore.setLayoutX(140);
        lbScore.setLayoutY(-8);
        lbScore.setText("0");
        lbScore.setFont(new Font(40));
        scorePane.getChildren().addAll(label,lbScore);

        lose = new Label("");
        lose.setLayoutX(140);
        lose.setLayoutY(110);

        pane.getChildren().addAll(linePane,scorePane,buttonPane);

        // 对鼠标点击控制运动方向的按钮添加事件响应。
        btDown.setOnAction(e->
                           {
                               Rectangle head = snake.getFirst();
                               if (!lose(head))
                                   turnDown();
                           });

        btUp.setOnAction(e->
                         {
                             Rectangle head = snake.getFirst();
                             if (!lose(head))
                                turnUp();
                         });

        btLeft.setOnAction(e->
                           {
                               Rectangle head = snake.getFirst();
                               if (!lose(head))
                                   turnLeft();
                           });

        btRight.setOnAction(e->
                            {
                                Rectangle head = snake.getFirst();
                                if (!lose(head))
                                    turnRight();
                            });

        // 对键盘控制运动方向的操作添加事件响应。
        pane.setOnKeyPressed(e->
                             {
                                 Rectangle head;
                                 switch (e.getCode())
                                 {
                                     case RIGHT:
                                         head = snake.getFirst();
                                         if (!lose(head))
                                             turnRight();
                                         break;
                                     case LEFT:
                                         head = snake.getFirst();
                                         if (!lose(head))
                                             turnLeft();
                                         break;
                                     case DOWN:
                                         head = snake.getFirst();
                                         if (!lose(head))
                                             turnDown();
                                         break;
                                     case UP:
                                         head = snake.getFirst();
                                         if (!lose(head))
                                             turnUp();
                                         break;
                                 }
                             });

        // 对重新开始游戏的按钮点击添加事件响应。
        btRestart.setOnAction(e->
                              {
                                  // 将整条蛇删除从界面上删除。
                                  for (Rectangle r : snake)
                                      linePane.getChildren().remove(r);
                                  // 删除蛇。
                                  snake.clear();

                                  // 删除苹果。
                                  linePane.getChildren().remove(apple);

                                  // 初始化新的小蛇。
                                  initialize();

                                  // 将小蛇在界面上显示出来。
                                  for (Rectangle r : snake)
                                      linePane.getChildren().add(r);

                                  // 随机生成新的苹果。
                                  apple = showApple();
                                  linePane.getChildren().add(apple);

                                  // 清空的得分
                                  lbScore.setText(String.valueOf(score));
                                  lose.setText("");

                                  // 重新开启动画
                                  animation.play();

                              });

        // 添加动画效果，使小蛇能自己动起来。
        EventHandler<ActionEvent> eventHandler = e->
        {
            Rectangle head;
            switch (direction)
            {
                case UP:
                    head = snake.getFirst();
                    if (!lose(head))
                        turnUp();
                    break;
                case DOWN:
                    head = snake.getFirst();
                    if (!lose(head))
                        turnDown();
                    break;
                case LEFT:
                    head = snake.getFirst();
                    if (!lose(head))
                        turnLeft();
                    break;
                case RIGHT:
                    head = snake.getFirst();
                    if (!lose(head))
                       turnRight();
                    break;
            }
        };

        animation =  new Timeline(new KeyFrame(Duration.millis(500),eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        // 为暂停键按钮添加鼠标点击的事件响应。
        btPause.setOnAction(e->
                            {
                                if (animation.getStatus() == Animation.Status.PAUSED)
                                {
                                    // 暂停游戏。
                                    animation.play();
                                    btPause.setText("Pause");
                                }
                                else
                                {
                                    // 继续游戏。
                                    animation.pause();
                                    btPause.setText("Resume");
                                }
                            });

        primaryStage.setTitle("Snake");
        primaryStage.setScene(new Scene(pane,600,320));
        primaryStage.show();
    }

    /**
     * 继承自Pane的LinePane类，用于绘制小蛇运动区域的网格。
     */
    class LinePane extends Pane
    {
        public LinePane()
        {
            // 画横线。
            Line horizontalLine[] = new Line[20];
            for (int i = 0; i < 20; i++)
            {
                horizontalLine[i] = new Line(0, i * 15, 300, i * 15);
                horizontalLine[i].setStroke(GRAY);
                this.getChildren().add(horizontalLine[i]);
            }

            // 画竖线。
            Line verticalLine[] = new Line[20];
            for (int i = 0; i < 20; i++)
            {
                verticalLine[i] = new Line(i * 15, 0, i * 15, 300);
                verticalLine[i].setStroke(GRAY);
                this.getChildren().add(verticalLine[i]);
            }

            // 画边界。
            Line border[] = new Line[4];
            border[0] = new Line(0,0,300,0);
            border[1] = new Line(0,300,300,300);
            border[2] = new Line(0,0,0,300);
            border[3] = new Line(300,0,300,300);
            for (int i = 0; i < 4; i++)
            {
                border[i].setStroke(BLACK);
                this.getChildren().add(border[i]);
            }
        }
    }

    /**
     * 继承自Button的BigButton类，用于使操作按钮样式一致。
     */
    class BigButton extends Button
    {
        public BigButton(String text)
        {
            super(text);
            setPrefWidth(70);
            setPrefHeight(40);
            setFont(new Font(13));
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
