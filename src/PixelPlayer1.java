import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PixelPlayer1 extends Player {
    PixelPlayer1(int[][] map) {
        super(map);
    }

    static final int SIZE_OF_BOARD = 8;
    int myCnt = 0, yourCnt = 0;
    int myNum, yourNum;
    ArrayList<Inform> myAl = new ArrayList<>();
    boolean firstChk;
    //myNum 1, 2, 0
    //nextPosition만 구현하면 됨!!
    public Point nextPosition(Point lastPosition) {
        int x = (int) lastPosition.getX(), y = (int) lastPosition.getY();
        myNum = map[(int) currentPosition.getX()][(int) currentPosition.getY()];
        if (myNum == 2) yourNum = 1;
        else yourNum = 2;
        Point nextPosition = new Point(0, 0);
        firstChk = true;

        System.out.println("myNum : " + myNum);

        if(firstChk){
            if(map[0][1]!=0&&map[1][0]!=0&&map[0][6]!=0&&map[1][7]!=0&&map[6][0]!=0&&map[7][1]!=0&&map[6][7]!=0
                    &&map[7][6]!=0&&map[1][1]!=0&&map[1][6]!=0&&map[6][1]!=0&&map[6][6]!=0){
                firstChk = false;
            }
            if((x==0||y==1)&&map[0][1]==0)
                return new Point(0, 1);
            else if((x==1||y==0)&&map[1][0]==0)
                return new Point(1, 0);
            else if((x==0||y==6)&&map[0][6]==0)
                return new Point(0, 6);
            else if((x==1||y==7)&&map[1][7]==0)
                return new Point(1, 7);
            else if((x==6||y==0)&&map[6][0]==0)
                return new Point(6, 0);
            else if((x==7||y==1)&&map[7][1]==0)
                return new Point(7, 1);
            else if((x==6||y==7)&&map[6][7]==0)
                return new Point(6, 7);
            else if((x==7||y==6)&&map[7][6]==0)
                return new Point(7, 6);
            else if((x==1||y==1)&&map[1][1]==0)
                return new Point(1, 1);
            else if((x==1||y==6)&&map[1][6]==0)
                return new Point(1, 6);
            else if((x==6||y==1)&&map[6][1]==0)
                return new Point(6, 1);
            else if((x==6||y==6)&&map[6][6]==0){
                //firstChk = false;
                return new Point(6, 6);
            }
        }

        for (int i = 0; i < 8; i++) {
            if (isAvailRange(i, y) && !isfour(i, y)) {
                nextPosition = new Point(i, y);
                chkMyCnt(i, y);
                chkYourCnt(i, y);
                myAl.add(new Inform(new Point(i, y), yourCnt+myCnt));
                //return nextPosition;
            }
            if (isAvailRange(x, i) && !isfour(x, i)) {
                nextPosition = new Point(x, i);
                chkMyCnt(x, i);
                chkYourCnt(x, i);
                myAl.add(new Inform(new Point(x, i), yourCnt+myCnt));
                //return nextPosition;
            }


        }
        Collections.sort(myAl, new NameAscCompare());
        for (int i = 0; i < myAl.size(); i++) {
            System.out.println(myAl.get(i).getPoint().getX() + ", " + myAl.get(i).getPoint().getY());
            System.out.println(myAl.get(i).getVal());
        }
        System.out.println("=========================");
        if (!myAl.isEmpty()) {
            nextPosition = myAl.get(0).getPoint();
            myAl.clear();
        }
        return nextPosition;

    }

    public void chkMyCnt(int x, int y) {
        int tmpx = x;
        int tmpy = y;
        myCnt = 0;
        //우
        while (isAvailRange2(tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌
        while (isAvailRange2(tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //상
        while (isAvailRange2(--tmpx, tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //하
        while (isAvailRange2(++tmpx, tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌상
        while (isAvailRange2(--tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌하
        while (isAvailRange2(++tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //우상
        while (isAvailRange2(--tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //우하
        while (isAvailRange2(++tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == myNum) myCnt++;
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        /*myAl.add(new Inform(new Point(x, y), myCnt));
        Point tmp = myAl.get(myAl.size() - 1).getPoint();
        int tmp2 = myAl.get(myAl.size() - 1).getVal();*/
        /*System.out.println(myCnt);
        System.out.println(tmp.getX() + " " + tmp.getY());
		System.out.println(tmp2);*/
    }

    public void chkYourCnt(int x, int y) {
        int tmpx = x;
        int tmpy = y;
        yourCnt = 0;
        //우
        while (isAvailRange2(tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌
        while (isAvailRange2(tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //상
        while (isAvailRange2(--tmpx, tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //하
        while (isAvailRange2(++tmpx, tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌상
        while (isAvailRange2(--tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //좌하
        while (isAvailRange2(++tmpx, --tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //우상
        while (isAvailRange2(--tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //우하
        while (isAvailRange2(++tmpx, ++tmpy)) {
            if (map[tmpx][tmpy] == yourNum) yourCnt++;
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        /*myAl.add(new Inform(new Point(x, y), myCnt));
        Point tmp = myAl.get(myAl.size() - 1).getPoint();
        int tmp2 = myAl.get(myAl.size() - 1).getVal();*/
        /*System.out.println(myCnt);
        System.out.println(tmp.getX() + " " + tmp.getY());
		System.out.println(tmp2);*/
    }


    public boolean isfour(int x, int y) {
        int i;
        int count = 0;
        map[x][y] = myNum;
        for (i = x - 3; i <= x + 3; i++) {
            if (i < 0 || i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[i][y] == myNum) {
                count++;
                if (count == 4) {
                    map[x][y] = 0;
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for (i = y - 3; i <= y + 3; i++) {
            if (i < 0 || i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[x][i] == myNum) {
                count++;
                if (count == 4) {
                    map[x][y] = 0;
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for (i = -3; i <= 3; i++) {
            if (x + i < 0 || y + i < 0 || x + i >= SIZE_OF_BOARD || y + i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[x + i][y + i] == myNum) {
                count++;
                if (count == 4) {
                    map[x][y] = 0;
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for (i = -3; i <= 3; i++) {
            if (x + i < 0 || y - i < 0 || x + i >= SIZE_OF_BOARD || y - i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[x + i][y - i] == myNum) {
                count++;
                if (count == 4) {
                    map[x][y] = 0;
                    return true;
                }
            } else {
                count = 0;
            }
        }
        map[x][y] = 0;
        return false;
    }

    //해당하는 좌표 값에 돌을 둘 수 있는지 확인
    public boolean isAvailRange(int x, int y) {
        if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7)) return false;

        if ((x >= 0 && x <= 7 && y >= 0 && y <= 7) && map[x][y] == 0) return true;
        else return false;
    }

    public boolean isAvailRange2(int x, int y) {
        if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7)) return false;

        if ((x >= 0 && x <= 7 && y >= 0 && y <= 7)) return true;
        else return false;
    }


    public class Inform {
        Point point;
        int val;

        public Inform(Point point, int val) {
            this.point = point;
            this.val = val;
        }

        public Point getPoint() {
            return point;
        }

        public int getVal() {
            return val;
        }
    }


    static class NameAscCompare implements Comparator<Inform> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(Inform arg0, Inform arg1) {
            //-1작다 0같다 1크다
            return arg0.getVal() < arg1.getVal() ? -1 : arg0.getVal() > arg1.getVal() ? 1 : 0;
        }

    }
}