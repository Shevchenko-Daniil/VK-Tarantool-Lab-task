package com.vk.vk_tarantool_task.no_double_tap;

import java.util.*;

public class Solution {


    public static void main(String[] args){

        // =======Предварительная инициализация=======

        // Созджание мапы с координатами цифр на клавиатуре
        HashMap<Integer, ArrayList<Point>> positions = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            positions.put(i, new ArrayList<>());
        }

        // =======Читаем данные=======
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        int M = scanner.nextInt();

        if(N < 2 || M > 100 || N % 2 != 0 || M % 2 != 0){
            System.out.println("Incorrect N or M");
            System.out.println("Answer: -1");
            return;
        }


        // Заполняем массив с клавиатурой и координатами цифр в ней
        int[][] keyBoard = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int number = scanner.nextInt();
                keyBoard[i][j] = number;
                positions.get(number).add(new Point(i, j));
            }
        }

        // Считываем номер телефона
        int sLen = scanner.nextInt();
        String strS = scanner.next();
        int[] S = strS.chars().map(c -> c - '0').toArray();


        // =======Контрольный вывод данных=======
        dataPrint(N, M, keyBoard, sLen, S, positions);


        // =======Решение=======
        int result = 0;
        int K = 0;
        List<Point> currPoints = positions.get(S[0]);
        List<Point> nextPoints = new ArrayList<>();
        for (int i = 0; i < sLen - 1; i++) {
            int symbol = S[i];

            nextPoints = positions.get(S[i + 1]);

            List<Point> possiblePoints = possiblePoints(currPoints, nextPoints);
            if(possiblePoints.isEmpty()){
                System.out.println("Answer: -1");
                return;
            }
            result+=symbol;
            K+=1;

            currPoints = possiblePoints;
        }
        result+=S[sLen - 1];
        System.out.println("Answer: " + K*result);



    }

    private static class Point{
        public int x;
        public int y;

        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString(){
            return "{" + x + ", " + y + "}";
        }
    }

    private static List<Point> possiblePoints(List<Point> startPoints, List<Point> endPoints){
        List<Point> result = new ArrayList<>();

        //возможные ходы коня
        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

        for(Point startP : startPoints) {
            for(Point endP : endPoints) {
                for (int i = 0; i < dx.length; i++) {
                    if (endP.x == startP.x + dx[i] && endP.y == startP.y + dy[i]) {
                        result.add(endP);
                    }
                }
            }
        }

        return result;
    }

    private static void dataPrint(int N, int M, int[][] keyBoard,
                                  int sLen, int[] S,
                                  HashMap<Integer, ArrayList<Point>> positions){
        System.out.println("N: " + N + ", M: " + M);
        System.out.println("=======KEYBOARD=======");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(keyBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=======NUMBER=======");
        for (int i = 0; i < sLen; i++) {
            System.out.print(S[i]);
        }
        System.out.println();
        System.out.println("=======POSITIONS=======");
        for (int key: positions.keySet()) {
            ArrayList<Point> row = positions.get(key);
            System.out.print(key + ":  ");
            for (int i = 0; i < row.size(); i++) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }
    }

}
