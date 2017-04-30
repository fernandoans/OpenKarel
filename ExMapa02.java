public class ExMapa02 extends ExMapa05 {
    
    public void run() {
        buscarSinalizador();
        deixarSinalizador();
    }
    
    public void deixarSinalizador() {
        while (beepersInBag()) {
            andarComDesvio(true);
            if (rightIsClear()) {
                putBeeper();
                andar();
            }
        }
    }
    
    public static void main(String [] args) {
        new ExMapa02();
    }
}