package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;


    @Entity
    public class CompressedImage {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getAttachedToId() {
            return attachedToId;
        }

        public void setAttachedToId(Long attachedToId) {
            this.attachedToId = attachedToId;
        }

        Long attachedToId;
        public CompressedImage() {
        }

        @Lob
        public byte[] getImageData() {
            return imageData;
        }

        public void setImageData(byte[] imageData) {
            this.imageData = imageData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public CompressedImage(byte[] imageData, int width, int height, String format, Long attachedToId) {
            this.imageData = imageData;
            this.width = width;
            this.height = height;
            this.format = format;
            this.attachedToId = attachedToId;
        }

        private byte[] imageData;
        private int width;
        private int height;
        private String format;
    }
