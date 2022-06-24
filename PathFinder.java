import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;


import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComboBox;

public class PathFinder{
    JFrame frame;
    private int cells=20;
        int delay=30;
        double dense=.5;
        double density=(cells*cells)*10;
        int startx=-1;
        int starty=-1;
        int finishx=-1;
        int finishy=-1;
        int tool=0;
        int checks=0;
        int length=0;
        int curAlg=0;
        int WIDTH=850;
        final int HEIGHT=650;
        final int Msize=600;
        int Csize=Msize/cells;
        String[] algorithm={"Dijkstra"};
        String[] tools={"Start","Finsh","Wall","Eraser"};

        boolean solving=false;

         Node[][] map;
         Algorithm Alg=new Algorithm();
        Random r=new Random();

        JSlider size=new JSlider(1,5,2);//Slider
        JSlider speed=new JSlider(0,500,delay);
        JSlider obstacles=new JSlider(1,100,50);

        JLabel algL=new JLabel("Algorith");// Lables
        JLabel toolL=new JLabel("ToolBox");
        JLabel sizeL=new JLabel("Size");
        JLabel cellsL=new JLabel(cells+"X"+cells);
        JLabel delayL=new JLabel("Delay");
        JLabel msL=new JLabel(delay+"ms");
        JLabel obstacaleL=new JLabel("Dens:");
        JLabel densityL=new JLabel(obstacles.getValue()+"%");
        JLabel checkL=new JLabel("Check:"+checks);
        JLabel lengthL=new JLabel("Path Length:"+length);

        JButton SearchB=new JButton("Search");//Buttons
        JButton resetB=new JButton("Reset");
        JButton genMapB=new JButton("Genratre Map");
        JButton clearB=new JButton("Clear");
        JButton creditB=new JButton("Credit");
        
        JComboBox algBx=new JComboBox(algorithm);
        JComboBox toolBx=new JComboBox(tools);//Drop down list

        JPanel toolP=new JPanel();//Panels

        Map canvas;//canvas
        
        Border lowerdetched=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
    public static void main(String[] args) {
        new PathFinder();
    }

    public PathFinder(){
        clearMap();
         initialize();
    }

    public void generateMap(){
        clearMap();
        for(int i=0;i<density;i++){
            Node current;
            do{
                int x=r.nextInt(cells);
                int y=r.nextInt(cells);
                current=map[x][y];// Random Node in the grid
            }while(current.getType()==2);//if there is a a find a new one
            current.setType(2);//set Node to be a wall
        }

    }

    public void clearMap(){
        finishx=-1;
        finishy=-1;
        startx=-1;
        starty=-1;
        map=new Node[cells][cells];
        for(int x=0;x<cells;x++){
            for(int y=0;y<cells;y++){
                map[x][y]=new Node(3,x,y);
            }
        }
       // reset();
    }

    public void resetMap(){
        for(int x=0;x<cells;x++){
            for(int y=0;y<cells;y++){
                Node curr=map[x][y];
                if(curr.getType()==4 || curr.getType()==5)//check if curr is checked or a final path
                map[x][y]=new Node(3, x, y);//Reset into empty Node
            }
        }
        if(startx>-1 && starty>-1){// reset start and finish
            map[startx][starty]=new Node(0, startx, starty);
            map[startx][starty].setHops(0);
        }
        if(finishx>-1 && finishy>-1)
        map[finishx][finishy]=new Node(1, finishx, finishy);
       // reset();//Reset the variables
    }

    public void initialize(){
        frame=new JFrame();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(WIDTH,HEIGHT);
        frame.setTitle("Path Finding Using Dijistra");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        toolP.setBorder(BorderFactory.createTitledBorder(lowerdetched,"Controls"));
        int space=25;
        int buff=45;

        toolP.setLayout(null);
        toolP.setBounds(10,10,210,600);

        SearchB.setBounds(40, space, 120, 25);
        toolP.add(SearchB);
        space+=buff;

        resetB.setBounds(40, space, 120, 25);
        toolP.add(resetB);
        space+=buff;

        genMapB.setBounds(40,space,120,25);
        toolP.add(genMapB);
        space+=buff;

        clearB.setBounds(40, space, 120, 25);
        toolP.add(clearB);
        space+=40;
        
        algL.setBounds(40, space, 120, 25);
        toolP.add(algL);
        space+=25;

        algBx.setBounds(40, space, 120, 25);
        toolP.add(algBx);
        space+=40;

        toolL.setBounds(40, space, 120, 25);
        toolP.add(toolL);
        space+=25;

        toolBx.setBounds(40, space, 120, 25);
        toolP.add(toolBx);
        space+=buff;

        sizeL.setBounds(15, space, 40, 25);
        toolP.add(sizeL);
        size.setMajorTickSpacing(10);
        size.setBounds(50, space, 100, 25);
        toolP.add(size);
        cellsL.setBounds(160, space, 40, 25);
        toolP.add(cellsL);
        space+=buff;

        delayL.setBounds(15, space, 50, 25);
        toolP.add(delayL);
        speed.setMajorTickSpacing(5);
        speed.setBounds(50, space, 100, 25);
        toolP.add(speed);
        msL.setBounds(160,space,40,25);
        toolP.add(msL);
        space+=buff;

        obstacaleL.setBounds(15, space, 100, 25);
        toolP.add(obstacaleL);
        obstacles.setMajorTickSpacing(5);
        obstacles.setBounds(50,space,100,25);
        toolP.add(obstacles);
        densityL.setBounds(160,space,100,25);
        toolP.add(densityL);
        space+=buff;

        checkL.setBounds(15,space,100,25);
        toolP.add(checkL);
        space+=buff;

        lengthL.setBounds(15,space,100,25);
        toolP.add(lengthL);
        space+=buff;

        creditB.setBounds(40,space,120,25);
        toolP.add(creditB);

        frame.getContentPane().add(toolP);

        canvas=new Map();
        canvas.setBounds(230,10,Msize+1,Msize+1);
        frame.getContentPane().add(canvas);

        SearchB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                reset();
                if((startx>-1&&starty>-1) &&(finishx>-1 &&finishy>-1))
                solving=true;
            }
        });

        resetB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                resetMap();
                Update();
            }
        });
        genMapB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generateMap();
                Update();
            }
        });
        clearB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                clearMap();;
                Update();
            }
        });
        toolBx.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                tool=toolBx.getSelectedIndex();
            }
        });
        size.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e){
            cells =size.getValue()*10;
            clearMap();;
            reset();
            Update();
         }   
        });
        speed.addChangeListener(new ChangeListener() {
           @Override
           public void stateChanged(ChangeEvent e){
            delay=speed.getValue();
            Update();
           } 
        });
        obstacles.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e){
            dense=(double)obstacles.getValue()/100;
            Update();
          
         }   
        });

        creditB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(frame, "message", "title", JOptionPane.PLAIN_MESSAGE,new ImageIcon(""));
            }
        });
        startSearch();
    }

    public void startSearch(){
        if(solving){
            switch(curAlg){
                case 0:
                Alg.Dijkstra();
            }
        }
        pause();
    }

    public void pause(){
        int i=0;
        while(!solving){
            i++;
            if(i>500)
            i=0;
            try{
                Thread.sleep(1);
            }catch(Exception e){
            }
        }
        startSearch();
    }

    public void Update(){
        density=(cells *cells)*dense;
        Csize=Msize/cells;
        canvas.repaint();
        cellsL.setText(cells+"X"+cells);
        msL.setText(delay+"ms");
        lengthL.setText("Path Length"+length);
        densityL.setText(obstacles.getValue()+"%");
        checkL.setText("Checks"+checks);
    }
    public void reset(){
        solving=false;
        length=0;
        checks=0;

    }
    public void delay(){
        try{
        Thread.sleep(delay);
        }catch(Exception e){}
    }

    class Map extends JPanel implements MouseListener,MouseMotionListener{
        public Map(){
            addMouseListener(this);
            addMouseMotionListener(this);
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for(int i=0;i<cells;i++){
                for(int j=0;j<cells;j++){
                    switch(map[i][j].getType()){
                        case 0:
                        g.setColor(Color.green);
                        break;
                        case 1:
                        g.setColor(Color.red);
                        break;
                        case 2:
                        g.setColor(Color.black);
                        break;
                        case 3:
                        g.setColor(Color.white);
                        break;
                        case 4:
                        g.setColor(Color.cyan);
                        break;
                        case 5:
                        g.setColor(Color.yellow);
                    }
                    g.fillRect(i*Csize, j*Csize,Csize, Csize);
                    g.setColor(Color.BLACK);
                    g.drawRect(i*Csize, j*Csize, Csize, Csize);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e){
            try{
                int x=e.getX()/Csize;
                int y=e.getY()/Csize;
                Node curr=map[x][y];
            if((tool==2|| tool==3)&& (curr.getType()!=0 && curr.getType()!=1))
            curr.setType(tool);
            Update();
            }catch(Exception w){}
        }
        @Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
        @Override
        public void mousePressed(MouseEvent e){
            resetMap();
            try{
                int x=e.getX()/Csize;
                int y=e.getY()/Csize;
                Node curr=map[x][y];
                switch(tool){
                    case 0:{
                        if(curr.getType()!=2){//If its not a wall
                            if(startx>-1 && starty>-1){
                            map[startx][starty].setType(3);
                            map[startx][starty].setHops(-1);
                        }
                        curr.setHops(0);
                        startx=x;
                        starty=y;
                        curr.setType(0);//set the Node to start
                    }
                    break;
                }
                case 1:{
                    if(curr.getType()!=2){
                        if(finishx>-1 && finishy>-1)
                        map[finishx][finishy].setType(3);
                        finishx=x;
                        finishy=y;
                        curr.setType(1);
                    }
                    break;
                }
                default:
                if(curr.getType()!=0 && curr.getType()!=1)
                curr.setType(tool);
                break;
            }
            Update();
        }catch(Exception Q){}
    }
    
		@Override
		public void mouseReleased(MouseEvent e) {
		}
    }

    class Algorithm{

        public void Dijkstra(){
            ArrayList<Node>priority =new ArrayList<>();
            priority.add(map[startx][starty]);
            while(solving){
                if(priority.size()<=0){
                    solving=false;
                    break;
                }
                int hops=priority.get(0).getHops()+1;
                ArrayList<Node> explore=exploreNeighbors(priority.get(0),hops);

                if(explore.size()>0){
                    priority.remove(0);
                    priority.addAll(explore);
                    Update();
                    delay();
                }else{
                    priority.remove(0);
                }
            }
        }
        public ArrayList<Node> exploreNeighbors(Node curr,int hops){
            ArrayList<Node> explored=new ArrayList<>();
            for(int a=-1;a<=1;a++ ){
                for(int b=-1;b<=1;b++){
                    int xbound=curr.getX()+a;
                    int ybound=curr.getY()+b;
                    if((xbound>-1 && xbound<cells)&& (ybound>-1 && ybound<cells)){
                        Node neighbor=map[xbound][ybound];
                        if((neighbor.getHops()==-1|| neighbor.getHops()>hops) && neighbor.getType()!=2){
                            explore(neighbor,curr.getX(),curr.getY(),hops);
                            explored.add(neighbor);
                        }
                    }
                }
            }
            return explored;
        }
    public void explore(Node curr,int lastX,int lastY,int hops){
    if(curr.getType()!=0 && curr.getType()!=1)
    curr.setType(4);
    curr.setLastNode(lastX, lastY);
    curr.setHops(hops);
    checks++;
    if(curr.getType()==1){
        backTracking(curr.getLastX(),curr.getLastY(),hops);
    }   
    }
    public void backTracking(int lx,int ly,int hops){
    length=hops;
    while(hops>1){
        Node curr=map[lx][ly];
        curr.setType(5);
        lx=curr.getLastX();
        ly=curr.getLastY();
        hops--;
    }
    solving=false;
    }
    }
        



    class Node{
    private int cellType=0;
    int hops,x,y,lastX,lastY;
    double dToEnd=0;

    public Node(int type,int x,int y){
        cellType=type;
        this.x=x;
        this.y=y;
        hops=-1;
    }
    public double getEuclidDist(){// calculate to find distance of finish Node
        int xdif=Math.abs(x-finishx);
        int ydif=Math.abs(y-finishy);
        dToEnd=Math.sqrt((xdif*xdif)+(ydif*ydif));
        return dToEnd;

    }
    public int getX(){return x;}//get Methods
    public int getY(){ return y;}
    public int getLastX(){ return lastX;}
    public int getLastY(){return lastY;}
    public int getType(){return cellType;}
    public int getHops(){return hops;}

    public void setType(int type){
        cellType=type;
    }
    public void setLastNode(int x,int y){
        lastX=x;
        lastY=y;
    }
    public void setHops(int hops){
        this.hops=hops;
    }



   } 

}
