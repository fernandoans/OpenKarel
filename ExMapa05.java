public class ExMapa05 extends BibliKarel {
    
    public void run() {
        buscarSinalizador();
    }
    
    public void buscarSinalizador() {
        do {
            while (andar()) pegarSinalizador();
        } while (up());
    }
    
    public static void main(String [] args) {
        new ExMapa05();
    }
}