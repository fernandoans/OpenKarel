public class ExMapa01 extends BibliKarel {

    public void run() {
        setPosOrig(true);
        do {
            andarComDesvio(false);
        } while (volta());
    }
    
    public static void main(String [] args) {
        new ExMapa01();
    }
}
