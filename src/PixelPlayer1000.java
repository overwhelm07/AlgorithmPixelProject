import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class PixelPlayer1000 extends Player {
    PixelPlayer1000(int[][] map) {
        super(map);
    }

    static final int SIZE_OF_BOARD = 8;
    static final int DISTANCE = 1;
    static final int MYCOUNT = 100;
    static final int YOURCOUNT = 50;
    int myCnt = 0, yourCnt = 0;
    int myNum, yourNum;
    ArrayList<Inform> myAl = new ArrayList<>();
    boolean firstChk;
    Random random;
    //myNum 1, 2, 0
    public Point nextPosition(Point lastPosition) {
        int x = (int) lastPosition.getX(), y = (int) lastPosition.getY();
        myNum = map[(int) currentPosition.getX()][(int) currentPosition.getY()];
        if (myNum == 2) yourNum = 1;
        else yourNum = 2;
        Point nextPosition = new Point(0, 0);
        firstChk = true;

        //System.out.println("myNum : " + myNum);

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

                //상대방이 두었을때 4목이 된 지점이면
                if(isfour2(i, y)){
                    myAl.add(new Inform(new Point(i, y), yourCnt+myCnt+10000));
                }else if(isfour2Avail(i, y)){//상대방이 두었을때 4목이 될 확률이 있는 경우
                    myAl.add(new Inform(new Point(i, y), yourCnt+myCnt+5000));
                }else if(isfourAvail(i, y)){//내가 두었을때 4목이 될 확률이 있는 경우
                    myAl.add(new Inform(new Point(i, y), yourCnt+myCnt+2500));
                }else {//아무것도 아닌경우
                    myAl.add(new Inform(new Point(i, y), yourCnt+myCnt));
                }
                //return nextPosition;
            }
            if (isAvailRange(x, i) && !isfour(x, i)) {
                nextPosition = new Point(x, i);
                chkMyCnt(x, i);
                chkYourCnt(x, i);

                if(isfour2(x, i)){
                    myAl.add(new Inform(new Point(x, i), yourCnt+myCnt+10000));
                }else if(isfour2Avail(x, i)){
                    myAl.add(new Inform(new Point(x, i), yourCnt+myCnt+5000));
                }else if(isfourAvail(x, i)){
                    myAl.add(new Inform(new Point(x, i), yourCnt+myCnt+2500));
                }else{
                    myAl.add(new Inform(new Point(x, i), yourCnt+myCnt));
                }
                //return nextPosition;
            }


        }
        Collections.sort(myAl, new NameAscCompare());
        /*for (int i = 0; i < myAl.size(); i++) {
            System.out.println(myAl.get(i).getPoint().getX() + ", " + myAl.get(i).getPoint().getY());
            System.out.println(myAl.get(i).getVal());
        }*/
        //System.out.println("=========================");
        //값이 적은 곳에 여러개가 있으면 그 중 랜덤값으로 리턴
        if (!myAl.isEmpty()) {
            random = new Random();
            int tmpVal = myAl.get(0).getVal();
            int tmpIdx = 0;
            for(int i=0; i<myAl.size(); i++){
                if(tmpVal == myAl.get(i).getVal()){
                    tmpIdx = i;
                }else{
                    break;
                }
            }

            nextPosition = myAl.get(random.nextInt(tmpIdx+1)).getPoint();
            myAl.clear();
        }
        return nextPosition;

    }
    /*
    주요 전략
    가로 세로 대각선에서 나의 돌과 상대 돌의 개수를 확인하여 돌의 개수가 가장 적은 곳에다 두기 위해 카운트를 한다
    그렇게 카운트를 해서 각 지점의 가중치를 계산한 후에 가장 가중치가 적은 값에 돌을 두면 4목이 될 확률이 적어지니깐 그 지점에 돌을 둔다
    만약 가중치 값이 똑같으면 랜덤함수를 이용해 같은 가중치 지점중 임의의 지점을 리턴한다
    2016.06.08(수)
    돌 개수 체크 할때 내 돌이 있으면 100증가 상대 돌이 있으면 50증가로 시험
    근데 그 돌의 거리가 내 지점에서 한칸씩 떨어질때마다 -5씩함
     */
    public void chkMyCnt(int x, int y) {
        int tmpx = x;
        int tmpy = y;
        int distance = 0;
        myCnt = 0;
        //우
        while (isAvailRange2(tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //좌
        while (isAvailRange2(tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //상
        while (isAvailRange2(--tmpx, tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        //하
        while (isAvailRange2(++tmpx, tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //좌상
        while (isAvailRange2(--tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //좌하
        while (isAvailRange2(++tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //우상
        while (isAvailRange2(--tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
            else if (map[tmpx][tmpy] == yourNum) {
                break;
            }
        }
        tmpx = x;
        tmpy = y;
        distance = 0;
        //우하
        while (isAvailRange2(++tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == myNum) myCnt+=(MYCOUNT-distance);
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
        int distance = 0;
        yourCnt = 0;
        //우
        while (isAvailRange2(tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //좌
        while (isAvailRange2(tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //상
        while (isAvailRange2(--tmpx, tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //하
        while (isAvailRange2(++tmpx, tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //좌상
        while (isAvailRange2(--tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //좌하
        while (isAvailRange2(++tmpx, --tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //우상
        while (isAvailRange2(--tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
            else if (map[tmpx][tmpy] == myNum) {
                break;
            }
        }
        distance = 0;
        tmpx = x;
        tmpy = y;
        //우하
        while (isAvailRange2(++tmpx, ++tmpy)) {
            distance+=DISTANCE;
            if (map[tmpx][tmpy] == yourNum) yourCnt+=(YOURCOUNT-distance);
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

    public boolean isfourAvail(int x, int y) {
        int i;
        int count = 0;
        map[x][y] = myNum;
        for (i = x - 3; i <= x + 3; i++) {
            if (i < 0 || i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[i][y] == myNum || map[i][y] == 0) {
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
            if (map[x][i] == myNum || map[x][i] == 0) {
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
            if (map[x + i][y + i] == myNum || map[x + i][y + i] == 0) {
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
            if (map[x + i][y - i] == myNum|| map[x + i ][y - i] == 0) {
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
    public boolean isfour2Avail(int x, int y) {
        int i;
        int count = 0;
        map[x][y] = yourNum;
        for (i = x - 3; i <= x + 3; i++) {
            if (i < 0 || i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[i][y] == yourNum || map[i][y] == 0) {
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
            if (map[x][i] == yourNum || map[x][i] == 0) {
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
            if (map[x + i][y + i] == yourNum || map[x+i][y+i] == 0) {
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
            if (map[x + i][y - i] == yourNum || map[x+i][y-i] == 0) {
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

    public boolean isfour2(int x, int y) {
        int i;
        int count = 0;
        map[x][y] = yourNum;
        for (i = x - 3; i <= x + 3; i++) {
            if (i < 0 || i >= SIZE_OF_BOARD) {
                continue;
            }
            if (map[i][y] == yourNum) {
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
            if (map[x][i] == yourNum) {
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
            if (map[x + i][y + i] == yourNum) {
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
            if (map[x + i][y - i] == yourNum) {
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