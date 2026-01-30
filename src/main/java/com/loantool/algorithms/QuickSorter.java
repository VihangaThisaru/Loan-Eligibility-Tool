package com.loantool.algorithms;

import com.loantool.models.Applicant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuickSorter {

    public List<Applicant> sort(List<Applicant> applicants) {
        if (applicants.size() <= 1) {
            return new ArrayList<>(applicants);
        }

        // Convert to array for sorting
        Applicant[] array = applicants.toArray(new Applicant[0]);
        quickSort(array, 0, array.length - 1);

        List<Applicant> sorted = new ArrayList<>();
        for (Applicant applicant : array) {
            sorted.add(applicant);
        }

        return sorted;
    }

    private void quickSort(Applicant[] array, int low, int high) {
        if (low < high) {
            // Partition the array
            int pivotIndex = partition(array, low, high);

            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(Applicant[] array, int low, int high) {
        // Randomized pivot selection for better average performance
        Random rand = new Random();
        int pivotIndex = low + rand.nextInt(high - low + 1);

        // Swap pivot with last element
        Applicant pivot = array[pivotIndex];
        array[pivotIndex] = array[high];
        array[high] = pivot;

        int i = low - 1;

        for (int j = low; j < high; j++) {
            // Compare applicants by risk score (descending order)
            if (array[j].compareTo(pivot) <= 0) {
                i++;

                // Swap array[i] and array[j]
                Applicant temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Place pivot at correct position
        Applicant temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

}