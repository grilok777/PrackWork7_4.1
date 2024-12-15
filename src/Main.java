import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {

        // Асинхронне завдання з незалежним логуванням
        CompletableFuture<Void> logTask = CompletableFuture.runAsync(() -> {
            System.out.println("Початок завдань...");
        });

        // Асинхронно генеруємо одновимірний масив з 10 чисел
        CompletableFuture<int[]> generateArray = CompletableFuture.supplyAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            int[] array = new Random().ints(10, 1, 200).toArray();
            System.out.println("Згенерований масив: " + Arrays.toString(array));
            System.out.println("Завдання 1 завершене за " + (System.currentTimeMillis() - taskStartTime) + " ms");
            return array;
        });

        // Модифікація масиву: додаємо 10 до кожного елемента
        CompletableFuture<double[]> addTen = generateArray.thenApplyAsync(array -> {
            long taskStartTime = System.currentTimeMillis();
            int[] modifiedArray = Arrays.stream(array).map(n -> n + 10).toArray();
            //System.out.println("Додано по 10: " + Arrays.toString(modifiedArray));
            System.out.println("Додано по 10. Завдання 2 завершене за " + (System.currentTimeMillis() - taskStartTime) + " ms");
            return Arrays.stream(modifiedArray).asDoubleStream().toArray();
        });

        // Ділення на 2 кожного елемента
        CompletableFuture<double[]> divideByTwo = addTen.thenApplyAsync(array -> {
            long taskStartTime = System.currentTimeMillis();
            double[] dividedArray = Arrays.stream(array).map(n -> n / 2.0).toArray();
            //System.out.println("Поділити на 2: " + Arrays.toString(dividedArray));
            System.out.println("Поділити на 2. Завдання 3 завершене за " + (System.currentTimeMillis() - taskStartTime) + " ms");
            return dividedArray;
        });

        // Виведення результату на екран із додатковим текстом
        CompletableFuture<Void> printResult = divideByTwo.thenAcceptAsync(array -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.println("Результат: " + Arrays.toString(array));
            System.out.println("Завдання 4 завершене за " + (System.currentTimeMillis() - taskStartTime) + " ms");
        });

        // Асинхронне логування завершення всіх задач
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(logTask, generateArray, addTen, divideByTwo, printResult)
                .thenRunAsync(() -> {
                    long taskStartTime = System.currentTimeMillis();
                    System.out.println("Усі завдання виконались за " + (System.currentTimeMillis() - taskStartTime) + " ms");
                });

        // Блокування потоку, поки всі завдання не завершаться
        allTasks.join();
    }
}