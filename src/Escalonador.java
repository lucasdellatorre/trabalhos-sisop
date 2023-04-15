import java.util.List;

public abstract class Escalonador {
    private List<Processo> blockedQueue;
    private List<Processo> readyQueue;
    private List<Processo> notStartedQueue;
    private Processo running;
    private Parser parser;

    abstract int run();
}