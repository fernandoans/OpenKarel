public class ExMapa06 extends BibliKarel {
    
    public void run() {
        obterSinalizadores();
        deixarSinalizadores();
    }
    
    public void obterSinalizadores() {
        do {
            while (beepersPresent()) {
                pickBeeper();
            }
        } while (andar());
    }
    
    public void deixarSinalizadores() {
        do {
            largar();
            subir();
            while (andar());
        } while (beepersInBag());
    }
    
    public void largar() {
        if (frontIsBlocked()) {
            putBeeper();
        }
    }
    
    public void subir() {
        turnLeft();
        while (rightIsBlocked()) {
            andar();
        }
        turnRight();
    }
    
    public static void main(String [] args) {
        new ExMapa06();
    }    
}