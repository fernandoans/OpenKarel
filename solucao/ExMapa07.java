import java.util.Random;

public class ExMapa07 extends BibliKarel {
    
    private Random rd = new Random();
    
    public void run() {
        do {
            andarAleatoriamente();
        } while (!beepersPresent());
    }
    
    private void andarAleatoriamente() {
        pegarSinalizador();
        andar();
        if (rd.nextBoolean()) {
            turnLeft();
        } else {
            turnRight();
        }
    }
    
    public static void main(String [] args) {
        new ExMapa07();
    }
}