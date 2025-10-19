package fr.istic.pra.tp_arbres;

import java.util.Scanner;

import fr.istic.pra.util.BinaryTreeImpl;
import fr.istic.pra.util.BinaryTreeImplProf;

/**
 * Classe principale pour lancer le benchmark des opérations sur les images.
 * @author Vincent Drevelle
 * @version 1.02, 2025-10-17
 */
public class Benchmark {
    /**
     * Point d'entrée principal de l'application.
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("=== Benchmark des opérations sur les images, avec TreeImage et BitmapImage ===");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println();

        // Configure the warmup
        fr.istic.pra.tp_arbres.benchmark.Benchmark warmup = new fr.istic.pra.tp_arbres.benchmark.Benchmark();
        warmup.minIterations = 10;
        warmup.minTimePerOpMs = 5;
        warmup.maxIterations = 100;

        // Configure the benchmark
        fr.istic.pra.tp_arbres.benchmark.Benchmark benchmark = new fr.istic.pra.tp_arbres.benchmark.Benchmark();
        benchmark.minIterations = 50;
        benchmark.minTimePerOpMs = 50;
        benchmark.maxIterations = 5000;
        
        // Ask the user for the BinaryTree implementation to use
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.print("Implémentation de BinaryTree à utiliser ? ([1]: BinaryTreeImplProf, 2: BinaryTreeImpl): ");
        benchmark.treeSupplier = BinaryTreeImplProf::new;
        warmup.treeSupplier = BinaryTreeImplProf::new;
        try {
            switch (scanner.useDelimiter("").nextInt()) {
                case 2 -> {
                    benchmark.treeSupplier = BinaryTreeImpl::new;
                    warmup.treeSupplier = BinaryTreeImpl::new;
                }
                default -> {
                    benchmark.treeSupplier = BinaryTreeImplProf::new;
                    warmup.treeSupplier = BinaryTreeImplProf::new;
                }
            }
        } catch (Exception e) {}
        System.out.println("Classe d'arbre binaire utilisée: " + benchmark.treeSupplier.get().getClass().getSimpleName());
        System.out.println();

        // Run the warmup
        System.out.println("=== Warmup ===");
        warmup.run();
        System.out.println();

        // Run the benchmark
        System.out.println("=== Benchmark ===");
        benchmark.run();
        System.out.println();

        // Print the results
        System.out.println("=== Résultats du benchmark ===");
        System.out.println();
        benchmark.printSummedResultsSortedBySize();
        System.out.println();
        benchmark.printSummedResultsByOperation();
        System.out.println();
        benchmark.printSummedResultsByOperation(name -> name.startsWith("a"));
        System.out.println();
        System.out.println("=== Fin des résultats du benchmark ===");
    }
}