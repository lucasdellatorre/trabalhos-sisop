import java.util.LinkedList;

public class Memory {
    private LinkedList<Partition> partitions;
    private final int MEMORY_SIZE;
    private int currentMemory;
    private String policy;

    public Memory (int memorySize) {
        MEMORY_SIZE = memorySize;
        currentMemory = MEMORY_SIZE;
        partitions = new LinkedList<>();
        partitions.add(new Hole(MEMORY_SIZE));
        policy = "worst-fit";
    }

    public Memory (int memSize, String policy) {
        MEMORY_SIZE = memSize;
        currentMemory = MEMORY_SIZE;
        partitions = new LinkedList<>();
        partitions.add(new Hole(MEMORY_SIZE));
        this.policy = policy;
    }

    public void alloc(Process process) throws InsufficientMemoryException {
        if (process.size > this.currentMemory ) {
            throw new InsufficientMemoryException();
        }

        switch (this.policy) {
            case "worst-fit":
                worstFit(process);
                break;
            case "circular-fit":
                circularFit(process);
                break;
            default:
                System.err.println("Memory allocation: Wrong Policy Name");
                break;
        }
    }

    private void worstFit(Process process) {
        int worstPartitionSize = 0;
        Hole worstHole = null;

        for (Partition partition : partitions) {
            if (partition instanceof Hole) {
                Hole hole = (Hole) partition;
                if (hole.size > worstPartitionSize) {
                    worstPartitionSize = hole.size;
                    worstHole = hole;
                }
            }
        }

        if (worstHole == null) {
            System.err.println("Memory: EMPTY HOLE");
            return;
        }

        worstHole.size -= process.size;
        currentMemory -= process.size;
        System.out.println("Process add: " + process + ", currentMem: " + currentMemory);
        partitions.add(process);
    }

    private void circularFit(Process process) { }

    public void free(String pid) {
        Process process = null;
        for (Partition partition : partitions) {
            if (partition instanceof Process) {
                Process candidateProcess = (Process) partition;
                if (candidateProcess.pid.equals(pid)) {
                    process = (Process) partition;
                }
            }
        }
        if (process == null) {
            System.err.println("Memory: Process not found");
            return;
        }

        currentMemory += process.size;
        System.out.println("Process removed: " + process + ", currentMem: " + currentMemory);
        partitions.remove(process);
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public void printMemoryState() {
        System.out.println("=============== MEMORY STATE ================");
        partitions.forEach(System.out::println);
        System.out.println("=============================================");
    }
}
