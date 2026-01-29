package com.loantool.algorithms;

import com.loantool.models.Applicant;

import java.util.ArrayList;
import java.util.List;

public class MergeSorter {

    public List<Applicant> sort(List<Applicant> applicants) {
        if (applicants.size() <= 1) {
            return new ArrayList<>(applicants);
        }

        Applicant[] array = applicants.toArray(new Applicant[0]);
        mergeSort(array, 0, array.length-1);
    }

    private void mergeSort(Applicant[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Recursively sort both halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Merge the sorted halves
            merge(array, left, mid, right);
        }
    }

    private void merge(Applicant[] array, int left, int mid, int right) {

    }
}
