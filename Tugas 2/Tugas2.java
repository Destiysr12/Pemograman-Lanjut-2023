import java.util.ArrayList;
import java.util.Scanner;

public class Tugas2 {
    public static void main(String[] args) {

        Scanner input_terminal = new Scanner(System.in);
        // inputan
        System.out.print("masukan panjang baris/kolom : ");
        int panjangMatriks = input_terminal.nextInt();

        // deklarasi matriks menggunakan ArrayList
        ArrayList<ArrayList<Integer>> matriks1 = new ArrayList<>();

        // inputan nilai matriks dan loopingnya
        for (int i = 0; i < panjangMatriks; i++) {
            matriks1.add(new ArrayList<>());
            for (int j = 0; j < panjangMatriks; j++) {
                System.out.print("baris ke " + (i + 1) + " kolom ke " + (j + 1) + " : ");
                matriks1.get(i).add(input_terminal.nextInt());
            }
        }
        System.out.println("\n");

        // tampilan matriks
        System.out.println("Hasil inputan Matriks");
        for (ArrayList<Integer> row : matriks1) {
            System.out.print("[");
            for (Integer value : row) {
                System.out.print(" " + value + " ");
            }
            System.out.print("]\n");
        }
        System.out.println("\n");

        ArrayList<ArrayList<Integer>> matriksTranspose = new ArrayList<>();
        // Membuat matriks transpose
        for (int a = 0; a < panjangMatriks; a++) {
            matriksTranspose.add(new ArrayList<>());
            for (int b = 0; b < panjangMatriks; b++) {
                matriksTranspose.get(a).add(matriks1.get(b).get(a));
            }
        }

        // tampilkan matriks transpose
        System.out.println("Matriks transpose");
        for (ArrayList<Integer> row : matriksTranspose) {
            System.out.print("[");
            for (Integer value : row) {
                System.out.print(" " + value + " ");
            }
            System.out.print("]\n");
        }
        System.out.println("\n");

        // menjumblahkan kedua matriks
        ArrayList<ArrayList<Integer>> matriksResult = new ArrayList<>();
        for (int i = 0; i < panjangMatriks; i++) {
            matriksResult.add(new ArrayList<>());
            for (int j = 0; j < panjangMatriks; j++) {
                matriksResult.get(i).add(matriks1.get(i).get(j) + matriksTranspose.get(i).get(j));
            }
        }
        System.out.println("\n");
        System.out.println("Hasil jumblah matriks1 dan matriksTranspose");
        for (ArrayList<Integer> row : matriksResult) {
            System.out.print("[");
            for (Integer value : row) {
                System.out.print(" " + value + " ");
            }
            System.out.print("]\n");
        }
    }
}
