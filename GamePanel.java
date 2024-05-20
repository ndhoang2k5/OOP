import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public abstract class GamePanel extends JPanel implements ActionListener {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int Speed = 150;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int index = 20;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public KeyListener startGame() {
        newApple();
        running = true;
        timer = new Timer(Speed, this);
        timer.start();
        return null;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            for(int i = 0;i < HEIGHT / UNIT_SIZE;++i){
                for(int j = 0;j < WIDTH / UNIT_SIZE;j++){
                    if ((i + j) % 2 == 0) {
                        g.setColor(new Color(217, 206, 107));
                        g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(203, 169, 83));
                        g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            for (int i = 0; i < HEIGHT / UNIT_SIZE; ++i) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
            }
            for (int i = 0; i < WIDTH / UNIT_SIZE; ++i) {
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (applesEaten % 5 == 0){
                    timer.setDelay(Speed - 50);
                } else {
                    timer.setDelay(Speed);
                }
                if (i == 0) {
                    g.setColor(new Color(231, 16, 16));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//                    ImageIcon imageIcon = new ImageIcon("snake.png");
//                    JLabel lable = new JLabel(imageIcon);
//                    g.drawImage(imageIcon.getImage(), x[i], y[i], null);

                } else {
                    g.setColor(new Color(196, 78, 15));
                    //if (applesEaten % 10 == 0) g.setColor(new Color(random.nextInt(255),random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("INK Free", Font.BOLD, 32));
            FontMetrics metricsScore = getFontMetrics(g.getFont());
            g.drawString("Your Score: " + applesEaten,(WIDTH - metricsScore.stringWidth("Your Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else gameOver(g);
    }


    public void newApple() {
        appleX = random.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
        fixApple();
    }
    private void fixApple(){
        for(int i = bodyParts;i > 0;i--){
            if (appleX == x[i] && appleY == y[i]){
                newApple();
            }
        }
    }
    public void move() {
        for(int i = bodyParts;i > 0;i--) {
            x[i] = x[i  - 1];
            y[i] = y[i - 1];
        }
        switch(direction) {
            case 'U' :
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        // check va cham vao than
        for(int i = bodyParts;i > 0;i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
         //va cham vao vien
        if (x[0] < 0 ) {
            running = false;
        }
        if (x[0] > WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > HEIGHT) {
            running = false;
        }
        if (!running) {
            timer .stop();
        }

    }
    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("INK Free", Font.BOLD, 32));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Your Score: " + applesEaten,(WIDTH - metricsScore.stringWidth("Your Score: " + applesEaten)) / 2, g.getFont().getSize());

        // gameOver
        g.setColor(Color.red);
        g.setFont(new Font("INK Free", Font.BOLD, 40));
        FontMetrics metricsOver = getFontMetrics(g.getFont());
        g.drawString("Game Over" ,(WIDTH - metricsOver.stringWidth("Game Over")) / 2, HEIGHT / 2);

        // newGame
        g.setColor(Color.PINK);
        g.setFont(new Font("INK Free", Font.BOLD, 40));

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            //pathFinder();
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public abstract void actionPerform(ActionEvent e);

    public class MyKeyAdapter extends KeyAdapter {
        @Override // di chuyen con ran
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
    private void pathFinder() {
        int ScoreA = 0;
        int ScoreB = 0;
        int ScoreC = 0;
        int xDistance;
        int yDistance;
        boolean blocked = false;
        int maxScoreA = 999999999;
        int maxScoreB = 999999999;
        int maxScoreC = 999999999;

        switch(direction) {
            case 'U':
                ScoreA = 0;
                ScoreB = 0;
                ScoreC = 0;
                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going up
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreA = 4;
                        }
                        ScoreA+= (xDistance * 10) + (yDistance * 10);
                        maxScoreA = ScoreA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if(x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreB = 4;
                        }
                        ScoreB+= (xDistance * 10) + (yDistance * 10);
                        maxScoreB = ScoreB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if(x[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going right
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreC = 4;
                        }
                        ScoreC+= (xDistance * 10) + (yDistance * 10);
                        maxScoreC = ScoreC + 14;
                    }
                    blocked = false;
                }

                if(maxScoreA <= maxScoreB && maxScoreA <= maxScoreC) {
                    direction = 'U';
                } else if (maxScoreB < maxScoreA && maxScoreB <= maxScoreC) {
                    direction = 'L';
                } else if(maxScoreC < maxScoreB && maxScoreC < maxScoreA) {
                    direction = 'R';
                }
                maxScoreA = 999999999;
                maxScoreB = 999999999;
                maxScoreC = 999999999;

                break;


            case 'D':
                ScoreA = 0;
                ScoreB = 0;
                ScoreC = 0;

                // If space to go down
                if (y[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreA = 4;
                        }
                        ScoreA += (xDistance * 10) + (yDistance * 10);
                        maxScoreA = ScoreA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if (x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreB = 4;
                        }
                        ScoreB += (xDistance * 10) + (yDistance * 10);
                        maxScoreB = ScoreB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if (x[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going right
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreC = 4;
                        }
                        ScoreC += (xDistance * 10) + (yDistance * 10);
                        maxScoreC = ScoreC + 14;
                    }
                    blocked = false;
                }

                if (maxScoreA <= maxScoreB && maxScoreA <= maxScoreB) {
                    direction = 'D';
                } else if (maxScoreB < maxScoreA && maxScoreB <= maxScoreC) {
                    direction = 'L';
                } else if (maxScoreC < maxScoreB && maxScoreC < maxScoreA) {
                    direction = 'R';
                }
                maxScoreA = 999999999;
                maxScoreB = 999999999;
                maxScoreC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'L':
                ScoreA = 0;
                ScoreB = 0;
                ScoreC = 0;

                // If space to go left
                if (x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreA = 4;
                        }
                        ScoreA += (xDistance * 10) + (yDistance * 10);
                        maxScoreA = ScoreA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreB = 4;
                        }
                        ScoreB += (xDistance * 10) + (yDistance * 10);
                        maxScoreB = ScoreB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going up
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreC = 4;
                        }
                        ScoreC += (xDistance * 10) + (yDistance * 10);
                        maxScoreC = ScoreC + 14;
                    }
                    blocked = false;
                }

                if (maxScoreA <= maxScoreB && maxScoreA <= maxScoreC) {
                    direction = 'L';
                } else if (maxScoreB < maxScoreA && maxScoreB <= maxScoreC) {
                    direction = 'D';
                } else if (maxScoreC < maxScoreB && maxScoreC < maxScoreA) {
                    direction = 'U';
                }

                maxScoreA = 999999999;
                maxScoreB = 999999999;
                maxScoreC = 999999999;

                break;
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'R':
                ScoreA = 0;
                ScoreB = 0;
                ScoreC = 0;


                // If space to go right
                if (x[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going right
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreA = 4;
                        }
                        ScoreA += (xDistance * 10) + (yDistance * 10);
                        maxScoreA = ScoreA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreB = 4;
                        }
                        ScoreB += (xDistance * 10) + (yDistance * 10);
                        maxScoreB = ScoreB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (!blocked) {
                        // Going up
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance != 0) {
                            ScoreC = 4;
                        }
                        ScoreC += (xDistance * 10) + (yDistance * 10);
                        maxScoreC = ScoreC + 14;
                    }
                    blocked = false;
                }

                if (maxScoreA <= maxScoreB && maxScoreA <= maxScoreC) {
                    direction = 'R';
                } else if (maxScoreB < maxScoreA && maxScoreB <= maxScoreC) {
                    direction = 'D';
                } else if (maxScoreC < maxScoreB && maxScoreC < maxScoreA) {
                    direction = 'U';
                }

                maxScoreA = 999999999;
                maxScoreB = 999999999;
                maxScoreC = 999999999;

                break;
        }
    }
}

