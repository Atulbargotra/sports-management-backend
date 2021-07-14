package com.atul.sportsmanagement.utils;

import com.atul.sportsmanagement.model.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ScheduleUtils {
    public static int log2WithCeil(int x){
        return (int)Math.ceil(Math.log(x)/Math.log(2));
    }
    public static int nextPowerOf2(int x){
        return (int)Math.pow(2,log2WithCeil(x));
    }
    public static int matchNumber = 1;
    public static void convertToObject(List<String> list, int round, List<Match> matches) {
        int j = 0;
        for(int i=0;i<list.size()/2;i++) {
            Match match = new Match();
            match.setMatchNumber(matchNumber);
            matchNumber++;
            match.setPlayerOne(list.get(j++));
            match.setPlayerTwo(list.get(j++));
            match.setRoundNumber(round);
            matches.add(match);
        }
    }
    public static List<Match> generateBySingleElimination(int teams, String[] names) {
        matchNumber = 1;
        shuffleArray(names);
        int rounds = (int) Math.ceil(Math.log(teams) / Math.log(2));
        int rem = nextPowerOf2(teams) - teams;
        int rem2 = teams - rem;
        int match = 1;
        List<String> temp = new ArrayList<>();
        List<String> byes = new ArrayList<>();
        List<Match> matches= new ArrayList<>();

        for (int round = 1; round <= rounds; round++) {
            if (round == 1 && rem2 != 0) {
                for (int j = rem; j < teams; j++) {
                    byes.add(names[j]);
                }
                convertToObject(byes, round, matches);
                for (int j = 0; j < rem; j++) {
                    temp.add(names[j]);
                }
                for (int i = 0; i < rem2 / 2; i++) {
                    temp.add("Winnner of match " + match);
                    match++;
                }
            } else {
                convertToObject(temp, round,matches);
                int size = temp.size();
                temp.clear();
                for (int j = 0; j < size / 2; j++) {
                    temp.add("Winner of match " + match);
                    match++;
                }
            }
        }
        return matches;
    }
    static void shuffleArray(String[] ar)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
