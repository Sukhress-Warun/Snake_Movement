import java.util.*;
import java.awt.Point;

class Snake {
    private static char b='O',f='F',em='.',h='H';
    private static List<Point> dir = new ArrayList<>();
    static{
        dir.add(new Point(1,0));
        dir.add(new Point(0,1));
        dir.add(new Point(-1,0));
        dir.add(new Point(0,-1));
    }

    private List <Point> body;
    private List <Point> food;
    private Point direction;
    private int rows,cols;
    private Boolean hadFood;
    private Boolean died;

    public Snake(int x, int y, int dx, int dy, int rows, int cols, int foodCount){
        this.body = new ArrayList<>();
        this.body.add(new Point(x,y));
        this.direction = new Point(dx,dy);
        this.rows = rows;
        this.cols = cols;
        this.hadFood = false;
        this.died = false;
        this.food = new ArrayList<>();
        this.generateFood(foodCount);
    }

    private void step(Boolean increaseSize){
        Point temp = this.nextHead();
        this.body.add(0,temp);
        if(!increaseSize){
            this.body.remove(this.body.size()-1);
        }
    }

    public Boolean move(Point p){
        if(p!=null){
            this.setDirection(p);
        }
        this.step(this.hadFood);
        this.hadFood = false;
        this.died = this.collideBody(false);
        this.hadFood = this.collideFood();
        return (!this.died);
    }

    private Boolean collideBody(Boolean nextHead){
        Point head;
		if (nextHead){
            head = this.nextHead();
        }
        else{
            head = this.getHead();
        }
        
        if ( head.x>=this.rows || head.x<0 || head.y>=this.cols || head.y<0){
            return true;
        }
        for(int i=1;i<this.body.size();i++){
            Point joint=this.body.get(i);
            if (head.x==joint.x && head.y==joint.y){
                return true;
            }
        }
        return false;
    }

    public void generateFood(int foodCount){
        // yet to change this implementation , (will take time to generate thus to change)

        if (foodCount>(this.rows*this.cols)-this.food.size()-this.body.size()){
            foodCount = (this.rows*this.cols)-this.food.size()-this.body.size();
        }
        Random r = new Random();
        for(int i=0;i<foodCount;i++){
            Point tempFood = new Point(r.nextInt(this.rows),r.nextInt(this.cols));
            while(this.food.contains(tempFood) || this.body.contains(tempFood)){
                tempFood = new Point(r.nextInt(this.rows),r.nextInt(this.cols));
            }
            this.food.add(tempFood);
        }
    }

    private Boolean collideFood(){
        Point head = this.getHead();
        if (this.food.contains(head)){
            this.food.remove(head);
            return true;
        }
        return false;
    }

    private void setDirection(Point p){
        this.direction.setLocation(p.x, p.y);
    }

    private Point nextHead(){
        Point temp = new Point(this.body.get(0));
        temp.translate(this.direction.x, this.direction.y);
        return temp;
    }

    public List<Point> getMovableDirections(){
        Point prev = this.getDirection();
		List<Point> movables = new ArrayList<Point>();
        for(Point p : Snake.dir){
            this.setDirection(p);
            if(!this.collideBody(true)){
                movables.add(new Point(p));
            }
        }
        this.setDirection(prev);
        return movables;
    }

    public char[][] getState(){
        char [][] state = new char[this.rows][this.cols];
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                Point temp = new Point(i,j);
                if(this.body.contains(temp)){
                    if(this.getHead().equals(temp)){
                        state[i][j] = Snake.h;
                    }
                    else{
                        state[i][j] = Snake.b;
                    }
                    
                }
                else if(this.food.contains(temp)){
                    state[i][j] = Snake.f;
                }
                else{
                    state[i][j] = Snake.em;
                }
            }
        }
        return state;
    }

    public Point getHead(){
        return new Point(this.body.get(0));
    }

    public Point getDirection(){
        return new Point(this.direction);
    }

    public List<Point> getFood(){
        List<Point> copy = new ArrayList<Point>();
        for(Point i : this.food){
            copy.add(new Point(i));
        }
        return copy;
    }

    public Boolean eaten(){
        return this.hadFood;
    }

    public Boolean isAlive(){
        return (!this.died);
    }
}

public class SnakeMovement {
    
    // setup
    static Random r = new Random();

    public static void main(String[] args) throws InterruptedException{
        // moving snake

        Snake s = new Snake(0,0,1,1,20,20,1);
        char state[][] = s.getState(); 
        SnakeMovement.printState(state);

        while(s.isAlive()){
            s.generateFood((s.eaten())?1:0);
            Point temp = SnakeMovement.direction(s);
            s.move(temp);
            state = s.getState();
            SnakeMovement.printState(state);
            Thread.sleep(20);
        }
    }    

    public static Point direction(Snake s){
        // choosing direction

        List<Point> temp = s.getMovableDirections();
        int len = temp.size();
        if(len==0){
            return null;
        }

        List<Point> food = s.getFood();
        if(food.size()==0){
            return temp.get(r.nextInt(temp.size()));
        }
        Point head = s.getHead();
        Point ansDirection = temp.get(0);

        int min = Math.abs(head.x+ansDirection.x-food.get(0).x)+Math.abs(head.y+ansDirection.y-food.get(0).y);
        for(Point f : food){
            for(Point d : temp){
                int dist = Math.abs((head.x+d.x)-f.x)+Math.abs((head.y+d.y)-f.y);
                if(dist<=min){
                    min = dist;
                    ansDirection = new Point(d);
                }
            }
        }
        return ansDirection;
    }

    public static void printState(char st[][]){
        // printing state of snake with board

        int r = st.length;
        int c = st[0].length;
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                System.out.print(st[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}