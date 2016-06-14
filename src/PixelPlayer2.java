import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class PixelPlayer2 extends Player {
    static final int SIZE_OF_BOARD = PixelTester.SIZE_OF_BOARD;

    static final int MY_STONE_WEIGHT = 10;
    static final int PARTNER_STONE_WEIGHT = 5;

    public int[][] weightMap = {
            { -1, 0, 0, 0, 0, 0, 0, -1 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { -1, 0, 0, 0, 0, 0, 0, -1 },
    };

    ArrayList<Point> preCandidate = new ArrayList<>(4);
    Random random = new Random();
    boolean b = false;


    PixelPlayer2(int[][] map) {
        super(map);
    }

    @Override
    void setCurrentPosition(Point currentPosition) {
        // 리셋버튼을 누른경우 가중치 배열 초기화
        if(PixelTester.start)
            clearWeightMap();

        //System.out.println("내가 놓고나서>>");
        updateWeight(currentPosition, MY_STONE_WEIGHT);

        preCandidate.clear();
    }

    @Override
    public Point nextPosition(Point lastPosition) {
        int lastX = (int)lastPosition.getX();
        int lastY = (int)lastPosition.getY();

        // 행, 열의 가중치 최소값
        int minWeight[] = { Integer.MAX_VALUE, Integer.MAX_VALUE };
        int minX[] = { -1, -1 };
        int minY[] = { -1, -1 };

        //System.out.println("상대방이 놓고나서>>");
        updateWeight(lastPosition, PARTNER_STONE_WEIGHT);

        // 행 슬라이더 조사
        for(int i = 0; i < SIZE_OF_BOARD; i++) {
            if(!isValidDolPosition(new Point(lastX, i), PixelTester.turn))
                continue;

            if(weightMap[lastX][i] < minWeight[0]) {
                minWeight[0] = weightMap[lastX][i];
                minX[0] = lastX;
                minY[0] = i;
            }
        }

        // 열 슬라이더 조사
        for(int i = 0; i < SIZE_OF_BOARD; i++) {
            if(!isValidDolPosition(new Point(i, lastY), PixelTester.turn))
                continue;

            if(weightMap[i][lastY] < minWeight[1]) {
                minWeight[1] = weightMap[i][lastY];
                minX[1] = i;
                minY[1] = lastY;
            }
        }


        int x = (int) lastPosition.getX();
        int y = (int) lastPosition.getY();

        // 가로 첫번째
        if(y == 0) {
            if(map[1][0] ==0)
                preCandidate.add(new Point(1, 0));
            if(map[6][0] ==0)
                preCandidate.add(new Point(6, 0));
        }
        // 가로 마지막
        if(y == 7) {
            if(map[1][7] ==0)
                preCandidate.add(new Point(1, 7));
            if(map[6][7] ==0)
                preCandidate.add(new Point(6, 7));
        }

        // 가로 두번째
        if(y == 1) {
            if(map[0][1] ==0)
                preCandidate.add(new Point(0, 1));
            if(map[1][1] ==0)
                preCandidate.add(new Point(1, 1));
            if(map[6][1] ==0)
                preCandidate.add(new Point(6, 1));
            if(map[7][1] ==0)
                preCandidate.add(new Point(7, 1));
        }
        // 가로 마지막 두번째
        if(y == 6) {
            if(map[0][6] ==0)
                preCandidate.add(new Point(0, 6));
            if(map[1][6] ==0)
                preCandidate.add(new Point(1, 6));
            if(map[6][6] ==0)
                preCandidate.add(new Point(6, 6));
            if(map[7][6] ==0)
                preCandidate.add(new Point(7, 6));
        }


        // 세로 첫번째
        if(x == 0) {
            if(map[0][1] == 0)
                preCandidate.add(new Point(0, 1));
            if(map[0][6] == 0)
                preCandidate.add(new Point(0, 6));
        }
        // 세로 마지막
        if(x == 7) {
            if(map[7][1] ==0)
                preCandidate.add(new Point(7, 1));
            if(map[7][6] ==0)
                preCandidate.add(new Point(7, 6));
        }

        // 세로 두번째
        if(x == 1) {
            if(map[1][0] ==0)
                preCandidate.add( new Point(1, 0));
            if(map[1][1] ==0)
                preCandidate.add( new Point(1, 1));
            if(map[1][6] ==0)
                preCandidate.add( new Point(1, 6));
            if(map[1][7] ==0)
                preCandidate.add( new Point(1, 7));
        }
        // 세로 마지막 두번째
        if(x == 6) {
            if(map[6][0] ==0)
                preCandidate.add( new Point(6, 0));
            if(map[6][1] ==0)
                preCandidate.add( new Point(6, 1));
            if(map[6][6] ==0)
                preCandidate.add( new Point(6, 6));
            if(map[6][7] ==0)
                preCandidate.add( new Point(6, 7));
        }


        if(!preCandidate.isEmpty() && b) {
            b = !b;

            Point resultPreCandidate;

            resultPreCandidate = preCandidate.get(0);
            for (Point e : preCandidate) {
                if(getCountOfNeighborhood(e, PixelTester.turn == 1 ? 2 : 1) < getCountOfNeighborhood(resultPreCandidate, PixelTester.turn == 1 ? 2 : 1))
                    resultPreCandidate = e;
                System.out.println(e);
            }

            return resultPreCandidate;
        }

        // NOTE 전략1 - 최종 2개 중 랜덤 선택
        int r = 0;

        // RANDOM, MINIMUM
        String STRATEGY = "MIN";

        if(STRATEGY.equalsIgnoreCase("RANDOM")) {
            // NOTE 전략1 - 최종 2개 중 랜덤 선택
            r = random.nextInt(2);
        } else if(STRATEGY.equalsIgnoreCase("MIN")) {
            // NOTE 전략2 - 최종 2개 중 더 작은 것 선택
            if (getCountOfNeighborhood(new Point(minX[0], minY[0]), PixelTester.turn == 1 ? 2 : 1) > getCountOfNeighborhood(new Point(minX[1], minY[1]), PixelTester.turn == 1 ? 2 : 1))
                r = 0;
            else
                r = 1;
        }

        /*System.out.println("*******************************");
        System.out.println(r);
        System.out.println(minX[0] + ", " + minY[0] + " // " + getCountOfNeighborhood(new Point(minX[0], minY[0]), PixelTester.turn == 1 ? 2 : 1));
        System.out.println(minX[1] + ", " + minY[1] + " // " + getCountOfNeighborhood(new Point(minX[1], minY[1]), PixelTester.turn == 1 ? 2 : 1));
        System.out.println(PixelTester.turn);
        System.out.println("*******************************");*/

        // 최종 선택된 놈의 좌표가 기본 (-1, -1)이라면 안타깝지만 패...
        // 기권표 던짐
        if(minX[r] < 0) {
            if(r == 0)
                r = 1;
            else
                r = 0;
        }

        if(minX[r] < 0) {
            return new Point(0, 0);
        }

        return new Point(minX[r], minY[r]);
    }

    public int getCountOfNeighborhood(Point p, int turn) {
        int num = 0;

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(p.x + i < 0 || p.y + j < 0 || p.x + i >= SIZE_OF_BOARD || p.y + j >= SIZE_OF_BOARD)
                    continue;

                if(map[p.x + i][p.y + j] == turn)
                    num++;
            }
        }
        return num;
    }

    public void updateWeight(Point p, int weight) {
        // 8-connect 이웃에 대해 가중치 갱신
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(p.x + i < 0 || p.y + j < 0 || p.x + i >= SIZE_OF_BOARD || p.y + j >= SIZE_OF_BOARD)
                    continue;

                if(weightMap[p.x + i][p.y + j] < 0)
                    continue;

                weightMap[p.x + i][p.y + j] += weight;
            }
        }


        //printWeightMap();
        //System.out.println();
    }

    public boolean isValidDolPosition(Point p, int who) {
        int beforeDol = 0;
        boolean isValid = false;

        // 돌이 이미 놓여있는 경우 or 모서리
        if(0 < map[p.x][p.y] || map[p.x][p.y] < 0)
            return false;

        // 돌을 한번 두어본다.
        beforeDol = map[p.x][p.y];
        map[p.x][p.y] = who;

        // 그리고 4개 여부 검사
        if(isFour(p, who)) {
            isValid = false;
        } else {
            isValid = true;
        }

        // 돌 원상복구
        map[p.x][p.y] = beforeDol;

        return isValid;
    }

    public boolean isFour(Point dolPosition, int who) {
        int i;
        int count = 0;
        int x = (int)dolPosition.getX();
        int y = (int)dolPosition.getY();

        for( i = x - 3; i <= x + 3; i++ ) {
            if ( i < 0 || i >= SIZE_OF_BOARD ) {
                continue;
            }
            if ( map[i][y] == who ) {
                count++;
                if ( count == 4 ) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for( i = y - 3; i <= y + 3; i++ ) {
            if ( i < 0 || i >= SIZE_OF_BOARD ) {
                continue;
            }
            if ( map[x][i] == who ) {
                count++;
                if ( count == 4 ) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for( i = -3; i <= 3; i++ ) {
            if ( x + i < 0 || y + i < 0 || x + i >= SIZE_OF_BOARD || y + i >= SIZE_OF_BOARD ) {
                continue;
            }
            if ( map[x + i][y + i] == who ) {
                count++;
                if ( count == 4 ) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for( i = -3; i <= 3; i++ ) {
            if ( x + i < 0 || y - i < 0 || x + i >= SIZE_OF_BOARD || y - i >= SIZE_OF_BOARD ) {
                continue;
            }
            if ( map[x + i][y - i] == who ) {
                count++;
                if ( count == 4 ) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        return false;
    }

    public void printWeightMap() {
        for(int i = 0; i < SIZE_OF_BOARD; i++) {
            for(int j = 0; j < SIZE_OF_BOARD; j++) {
                System.out.print(String.format("%3d ", weightMap[i][j]));
            }
            System.out.println();
        }
    }
    public void clearWeightMap() {
        for(int i = 0; i < SIZE_OF_BOARD; i++) {
            for(int j = 0; j < SIZE_OF_BOARD; j++) {
                weightMap[i][j] = 0;
            }
        }

        weightMap[0][0] = -1;
        weightMap[0][7] = -1;
        weightMap[7][0] = -1;
        weightMap[7][7] = -1;

        b = false;
    }
}