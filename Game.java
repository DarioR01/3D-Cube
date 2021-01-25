import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Vector;

public class Game extends Canvas implements Runnable {
    final int width=1280;
    final int height=720;


    double theta=90.0;
    float fFov= (float)Math.toRadians(theta);
    float rTheta=fFov;
    float fNear=0.1f;
    float fFar=1000.0f;
    float fAspectRatio=(float)height/width;
    float[][] ProjectionMatrix={{(float)((fAspectRatio)*(1/(Math.tan(fFov/2)))),0,0,0},{0,(float)(1/((Math.tan(fFov/2)))),0,0},{0,0,(float)(fFar/(fFar-fNear)),1},{0,0,(-(fFar*fNear))/(fFar-fNear),0}};

    private Thread thread;
    private boolean running=false;

    Cube cube= new Cube();

    public Game(){
        new Window("Cube",width,height,this);
    }

    public synchronized void start(){
        thread=new Thread(this);
        thread.start();
        running=true;
    }
    
    public synchronized void stop(){
        try{
            thread.join();
            running=false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
         long lastTime = System.nanoTime();
         double amountOfTicks = 60.0;
         double ns= 1000000000/amountOfTicks;
         double delta=0;
         long timer=System.currentTimeMillis();
         int frames=0;
         while(running){
             long now=System.nanoTime();
             delta+= (now-lastTime)/ns;
             lastTime=now;
             while(delta>=1){
                 tick();
                 delta--;
             }
             if(running){
                 render();
             }
             frames++;
             if(System.currentTimeMillis()-timer>1000){
                 timer+=1000;
                 System.out.println("FPS: "+frames);
                 frames=0;
             }
         }
         stop();
    }

    public void tick(){
        cube= new Cube();
        rTheta+=0.01f;
        if(rTheta==10000){
            rTheta=0;
        }
        float[][] matRotz={{((float)Math.cos(rTheta)),((float)Math.sin(rTheta)),0,0},{((float)-(Math.sin(rTheta))),((float)Math.cos(rTheta)),0,0},{0,0,1,0},{0,0,0,1}};
        float[][] matRotx={{1,0,0,0},{0,((float)Math.cos(rTheta*0.5)),((float)Math.sin(rTheta*0.5)),0},{0,((float)-(Math.sin(rTheta*0.5))),((float)Math.cos(rTheta*0.5)),0},{0,0,0,1}};
        for(int i=0;i<cube.meshCube.tris.size();i++){
            for(int j=0;j<3;j++){
                
                cube.meshCube.tris.get(i).p[j].pos=matrixMultiplication(cube.meshCube.tris.get(i).p[j].pos,matRotz);
                cube.meshCube.tris.get(i).p[j].pos=matrixMultiplication(cube.meshCube.tris.get(i).p[j].pos,matRotx);            
                cube.meshCube.tris.get(i).p[j].pos[0][2]+=3.0f;

                cube.meshCube.tris.get(i).p[j].pos=matrixMultiplication(cube.meshCube.tris.get(i).p[j].pos,ProjectionMatrix);

                if(cube.meshCube.tris.get(i).p[j].pos[0][3]!=0){
                    cube.meshCube.tris.get(i).p[j].pos[0][0]/=cube.meshCube.tris.get(i).p[j].pos[0][3];
                    cube.meshCube.tris.get(i).p[j].pos[0][1]/=cube.meshCube.tris.get(i).p[j].pos[0][3];
                    cube.meshCube.tris.get(i).p[j].pos[0][2]/=cube.meshCube.tris.get(i).p[j].pos[0][3];
                }

                cube.meshCube.tris.get(i).p[j].pos[0][0]+=1.0f;
                cube.meshCube.tris.get(i).p[j].pos[0][1]+=1.0f;

                cube.meshCube.tris.get(i).p[j].pos[0][0]*=(float)0.5f*width;
                cube.meshCube.tris.get(i).p[j].pos[0][1]*=(float)0.5f*height;
            }
        }
    }

    private void render(){
        BufferStrategy bs=this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g=bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.green);
        
        for(int i=0;i<cube.meshCube.tris.size();i++){
            drawTriangle(g, (int)cube.meshCube.tris.get(i).p[0].pos[0][0], (int) cube.meshCube.tris.get(i).p[0].pos[0][1], (int)cube.meshCube.tris.get(i).p[1].pos[0][0], (int)cube.meshCube.tris.get(i).p[1].pos[0][1],(int) cube.meshCube.tris.get(i).p[2].pos[0][0],(int) cube.meshCube.tris.get(i).p[2].pos[0][1]);
        }

        g.dispose();
        bs.show();
    }
    
    
    public static void main(String[] args) {
        new Game();
    }

    public static float[][] matrixMultiplication(float[][] a, float[][] b){
        float[][] result=new float[a.length][b[0].length];
        float temp=0;
        if(a[0].length==b.length){
            for(int i=0;i<a.length;i++){
                for(int j=0;j<b[0].length;j++){
                   for(int z=0;z<a[0].length;z++){
                       temp+=a[i][z]*b[z][j];
                    }
                   result[i][j]=temp;
                   temp=0;
                }
            }
            return result;
        }
        else{
            System.out.println("Error, impossible to multiply these matrices due to their size");
            return a;
        }
    }

    public static void drawTriangle(Graphics g, int x1, int y1,int x2,int y2,int x3, int y3){
        g.drawLine(x1,y1,x2,y2);
        g.drawLine(x2,y2,x3,y3);
        g.drawLine(x3,y3,x1,y1);
    }

    public static void printMatrix(float[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.print("\n");
        }
    }
}