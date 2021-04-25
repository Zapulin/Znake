import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Znake extends JFrame {

    ImagenSnake imagenSnake;

    Point snake;
    Point comida;
    Point cactus;
    ArrayList<Point> colaSnake = new ArrayList<Point>();

    int longitud;

    int width = 640;
    int height = 480;

    int widthPoint = 20;
    int heightPoint = 20;

    String direccion = "RIGHT";
    long frequency = 55;

    boolean gameOver = false;


    public Znake() {
        setTitle("Znake");

        startGame();

        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        setSize(width,height);
        //setLayout(null);

        this.addKeyListener(new Teclas());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);
        Refresh refresh = new Refresh();
        Thread trid = new Thread(refresh);
        trid.start();
    }

    public void startGame() {
        comida = new Point(200,100);
        snake = new Point(320,240);
        cactus = new Point(400,400);
        colaSnake  = new ArrayList<Point>();
        colaSnake.add(snake);

        longitud = colaSnake.size();
    }

    public void generarComida() {
        Random rnd = new Random();

        comida.x = (rnd.nextInt(width));
        if((comida.x % 20) > 0) {
            comida.x = comida.x - (comida.x % 20);
        }

        if(comida.x < 20) {
            comida.x = comida.x + 20;
        }
        if(comida.x > width) {
            comida.x = comida.x - 20;
        }

        comida.y = (rnd.nextInt(height));
        if((comida.y % 20) > 0) {
            comida.y = comida.y - (comida.y % 20);
        }

        if(comida.y > height) {
            comida.y = comida.y - 20;
        }
        if(comida.y < 0) {
            comida.y = comida.y + 20;
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

        if((snake.x > (comida.x-20) && snake.x < (comida.x+20)) && (snake.y > (comida.y-20) && snake.y < (comida.y+20))) {
            colaSnake.add(0,new Point(snake.x,snake.y));
            System.out.println(colaSnake.size());
            generarComida();
        }

        if((snake.x > (cactus.x-20) && snake.x < (cactus.x+20)) && (snake.y > (cactus.y-40) && snake.y < (cactus.y+40))) {
            gameOver = true;
        }
        imagenSnake.repaint();

    }

    public class ImagenSnake extends JPanel {


        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(gameOver) {

        
            } else {

                Image fondo = new ImageIcon(getClass().getResource("/images/background.jpg")).getImage();
                g.drawImage(fondo, 0,0, getWidth(), getHeight(), this);

            }
            g.setColor(new Color(136, 213, 50));

            if(colaSnake.size() > 0) {
                for(int i=0;i<colaSnake.size();i++) {
                    Point p = (Point)colaSnake.get(i);
                    g.fillRect(p.x,p.y,widthPoint,heightPoint);
                }
            }

            g.setColor(new Color(172, 7, 7));

            Image manzana = new ImageIcon(getClass().getResource("/images/manzana snake.png")).getImage();
            g.drawImage(manzana, comida.x,comida.y, 20, 20, this);

            if(gameOver) {

                Image over = new ImageIcon(getClass().getResource("/images/gameover.png")).getImage();
                g.drawImage(over, 0,0, getWidth(), getHeight(), this);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString(""+ (colaSnake.size()-1), 330, 428);

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

    public class Refresh extends Thread {

        private long last = 0;

        public Refresh() {

        }

        public void run() {
            while(true) {
                if((java.lang.System.currentTimeMillis() - last) > frequency) {
                    if(!gameOver) {

                        if(direccion == "RIGHT") {
                            snake.x = snake.x + widthPoint;
                            if(snake.x > width - widthPoint) {
                                snake.x = 0;
                            }
                        } else if(direccion == "LEFT") {
                            snake.x = snake.x - widthPoint;
                            if(snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                        } else if(direccion == "UP") {
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
        Znake snake = new Znake();
    }

}
