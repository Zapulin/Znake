import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;


public class Znake extends JFrame {

    ImagenSnake imagenSnake;
    Point snake;
    Point comida;
    ArrayList<Point> colaSnake = new ArrayList<Point>();

    int longitud;

    int width = 640;
    int height = 480;

    int widthPoint = 10;
    int heightPoint = 10;

    String direccion = "RIGHT";
    long frequency = 50;

    boolean gameOver = false;

    public Znake() {
        setTitle("Znake");

        startGame();
        imagenSnake = new ImagenSnake();

        this.getContentPane().add(imagenSnake);

        setSize(width,height);

        this.addKeyListener(new Teclas());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        setVisible(true);
        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame() {
        comida = new Point(200,100);
        snake = new Point(320,240);
        colaSnake  = new ArrayList<Point>();
        colaSnake.add(snake);

        longitud = colaSnake.size();
    }

    public void generarComida() {
        Random rnd = new Random();

        comida.x = (rnd.nextInt(width));
        if((comida.x % 10) > 0) {
            comida.x = comida.x - (comida.x % 10);
        }

        if(comida.x < 10) {
            comida.x = comida.x + 10;
        }
        if(comida.x > width) {
            comida.x = comida.x - 10;
        }

        comida.y = (rnd.nextInt(height));
        if((comida.y % 10) > 0) {
            comida.y = comida.y - (comida.y % 10);
        }

        if(comida.y > height) {
            comida.y = comida.y - 10;
        }
        if(comida.y < 0) {
            comida.y = comida.y + 10;
        }

    }

    public void actualizar() {

        colaSnake  .add(0,new Point(snake.x,snake.y));
        colaSnake.remove(colaSnake.size()-1);

        for (int i=1;i<colaSnake.size();i++) {
            Point point = colaSnake.get(i);
            if(snake.x == point.x && snake.y  == point.y) {
                gameOver = true;
            }
        }

        if((snake.x > (comida.x-10) && snake.x < (comida.x+10)) && (snake.y > (comida.y-10) && snake.y < (comida.y+10))) {
            colaSnake.add(0,new Point(snake.x,snake.y));
            System.out.println(colaSnake.size());
            generarComida();
        }
        imagenSnake.repaint();

    }


    public class ImagenSnake extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(gameOver) {
                g.setColor(new Color(0,0,0));
            } else {

                g.setColor(new Color(113, 109, 109));
            }
            g.fillRect(0,0, width, height);
            g.setColor(new Color(136, 213, 50));

            if(colaSnake.size() > 0) {
                for(int i=0;i<colaSnake.size();i++) {
                    Point p = (Point)colaSnake.get(i);
                    g.fillRect(p.x,p.y,widthPoint,heightPoint);
                }
            }

            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x,comida.y,widthPoint,heightPoint);

            if(gameOver) {
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                g.drawString("GAME OVER", 300, 200);
                g.drawString("SCORE "+(colaSnake.size()-1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("R to Replay", 100, 320);
                g.drawString("ESC to Exit", 100, 340);
            }

        }
    }

    public class Teclas extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {

                if(direccion != "LEFT") {
                    direccion = "RIGHT";

                }
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(direccion != "RIGHT") {
                    direccion = "LEFT";
                }
            } else if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(direccion != "DOWN") {
                    direccion = "UP";
                }
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(direccion != "UP") {
                    direccion = "DOWN";
                }

            } else if(e.getKeyCode() == KeyEvent.VK_R) {
                gameOver = false;
                startGame();
            }
        }

    }

    public class Momento extends Thread {

        private long last = 0;

        public Momento() {

        }

        public void run() {
            while(true) {
                if((java.lang.System.currentTimeMillis() - last) > frequency) {
                    if(!gameOver) {

                        if(direccion == "RIGHT") { // PERECTO
                            snake.x = snake.x + widthPoint;
                            if(snake.x > width - widthPoint) {
                                snake.x = 0;
                            }
                        } else if(direccion == "LEFT") { //PERFECTO
                            snake.x = snake.x - widthPoint;
                            if(snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                        } else if(direccion == "UP") {  //PERFECTO
                            snake.y = snake.y - heightPoint;
                            if(snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                        } else if(direccion == "DOWN") {
                            snake.y = snake.y + heightPoint;
                            if(snake.y > height - heightPoint) {
                                snake.y = 0;
                            }
                        }
                    }
                    actualizar();

                    last = java.lang.System.currentTimeMillis();
                }

            }
        }
    }
    
    public static void main(String[] args) {
        Znake snake1 = new Znake();
    }

}