public class vec3d {
    float[][] pos;

    public vec3d(float x, float y, float z){
        float[][] temp={{x,y,z,1}};
        this.pos=temp;
    }
}