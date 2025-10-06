package com.vk.vk_tarantool_task.double_tap;

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

        // Превращаем номер в массив чисел и считаем сумму всех цифр
        int result = 0;
        int[] S = new int[sLen];
        int cnt = 0;
        for(char c : strS.toCharArray()){
            S[cnt] = c - '0';
            result+=S[cnt];
            cnt++;
        }


        // =======Контрольный вывод данных=======
        dataPrint(N, M, keyBoard, sLen, S, positions);


        // =======Решение=======
        // Проверка на отсутствие точки старта
        if(sLen == 1 && positions.get(S[0]).isEmpty()){
            System.out.println("Answer: -1");
            return;
        }

        int K = 0; // Количество переходов

        Stack<Integer> zeroLevels = new Stack<>();// Стэк индексов чиcел, где можно остаться на месте
        Stack<List<Point>> zeroLevelsPoints = new Stack<>(); //Стэк возможных точек для соответсвующих 'нулевых' индексов
        int lastCheckedLevel = -1;

        List<Point> currPoints = positions.get(S[0]);
        List<Point> nextPoints = new ArrayList<>();
        for (int i = 0; i < sLen - 1; i++) {
            int symbol = S[i];

            // Если следующая цифра в номере такая же, то сначала проверяем кейс, чтобы остаться на той же позиции
            if (S[i+1] == symbol && (zeroLevels.empty() || i != lastCheckedLevel)) {
                zeroLevels.push(i);
                zeroLevelsPoints.push(currPoints);
                continue;
            }


            nextPoints = positions.get(S[i + 1]);

            // получаем список всех возможных точек следующей цифры
            List<Point> possiblePoints = possiblePoints(currPoints, nextPoints);

            if(possiblePoints.isEmpty() && zeroLevels.empty()){
                System.out.println("Answer: -1");
                return;
            }
            // если проверяли кейс двойного нажатия и он не сработал, то откатываемся назад
            else if (possiblePoints.isEmpty() && !zeroLevels.empty()) {
                K = K - (i - zeroLevels.peek() - 1);
                i = zeroLevels.peek() - 1; // -1 потому что в следующей итерации цикла выполнится i++
                lastCheckedLevel = i + 1;
                currPoints = zeroLevelsPoints.pop();
                zeroLevels.pop();
                continue;
            }
            K+=1;

            currPoints = possiblePoints;
        }
        System.out.println("=======Answer=======");
        System.out.println("K: " + K + "\nC: " + K*result);



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
                    if (endP.x == startP.x + dx[i] && endP.y ==startP.y + dy[i]) {
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
