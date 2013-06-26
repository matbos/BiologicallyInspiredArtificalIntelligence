package pl.polsl.bos.ann;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class DirectoryList {
        public String name;
        private double[] result;
        public ArrayList<File> files;
        public ArrayList<BufferedImage> images;

        static byte counter = 0;

        @SuppressWarnings("unused")
        private DirectoryList() {
            images = new ArrayList<BufferedImage>(10);
        }

        public DirectoryList(String name, double[] result, File[] files) {
            this();
            this.name = name;
            this.result = result;
            createArrayListFromArray(files);
        }

        @SuppressWarnings("unused")
        public DirectoryList(String name, File[] files) {
            this();
            this.name = name;
            createArrayListFromArray(files);
        }

        public void addImage(BufferedImage image) {
            images.add(image);
        }

        public BufferedImage getNextImage() {
            BufferedImage image = null;
            try{
                image = images.get(counter++);
            } catch (IndexOutOfBoundsException e){
                counter = 0;
                image = images.get(counter++);
            }
            return image;
        }
        public double[] getProperOutcome(){
            return result;
        }

        private void createArrayListFromArray(File[] files) {
            this.files = new ArrayList<File>(files.length);
            for (File f : files) {
                this.files.add(f);
            }
        }
}