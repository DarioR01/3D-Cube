public class Cube {
    mesh meshCube= new mesh();

    public Cube(){
        //South
        meshCube.tris.add(new triangle(new vec3d(0,0,0),new vec3d(0,1,0),new vec3d(1,1,0)));
        meshCube.tris.add(new triangle(new vec3d(0,0,0),new vec3d(1,1,0),new vec3d(1,0,0)));

        //East
        meshCube.tris.add(new triangle(new vec3d(1,0,0),new vec3d(1,1,0),new vec3d(1,1,1)));
        meshCube.tris.add(new triangle(new vec3d(1,0,0),new vec3d(1,1,1),new vec3d(1,0,1)));

        //North
        meshCube.tris.add(new triangle(new vec3d(1,0,1),new vec3d(1,1,1),new vec3d(0,1,1)));
        meshCube.tris.add(new triangle(new vec3d(1,0,1),new vec3d(0,1,1),new vec3d(0,0,1)));

        //West
        meshCube.tris.add(new triangle(new vec3d(0,0,1),new vec3d(0,1,1),new vec3d(0,1,0)));
        meshCube.tris.add(new triangle(new vec3d(0,0,1),new vec3d(0,1,0),new vec3d(0,0,0)));

        //Top
        meshCube.tris.add(new triangle(new vec3d(0,1,0),new vec3d(0,1,1),new vec3d(1,1,1)));
        meshCube.tris.add(new triangle(new vec3d(0,1,0),new vec3d(1,1,1),new vec3d(1,1,0)));

        //Bottom
        meshCube.tris.add(new triangle(new vec3d(1,0,1),new vec3d(0,0,1),new vec3d(0,0,0)));
        meshCube.tris.add(new triangle(new vec3d(1,0,1),new vec3d(0,0,0),new vec3d(1,0,0)));
    }
}