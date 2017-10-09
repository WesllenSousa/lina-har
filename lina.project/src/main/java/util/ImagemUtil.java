package util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImagemUtil {

    public static BufferedImage criaBufferedImage(Image imagem) {
        BufferedImage imagemBuffer = new BufferedImage(imagem.getWidth(null), imagem.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        return imagemBuffer;
    }

    public static Image lerImagemToolKit(URL file) {
        Image imagem = Toolkit.getDefaultToolkit().getImage(file);
        return imagem;
    }

    public static BufferedImage ler(String arquivo) {
        try {
            return (ImageIO.read(new File(arquivo)));
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static BufferedImage ler(File arquivo) {
        try {
            return (ImageIO.read(arquivo));
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static BufferedImage ler(InputStream input) {
        try {
            return ImageIO.read(input);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void salvar(BufferedImage bufferedImage, String sufixo, String arquivo) {
        try {
            ImageIO.write(bufferedImage, sufixo, new File(arquivo));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void salvar(BufferedImage bufferedImage, String sufixo, OutputStream output) {
        try {
            ImageIO.write(bufferedImage, sufixo, output);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void salvar(BufferedImage bufferedImage, String arquivo) {
        try {
            String sufixo = FileUtil.getFileExtension(arquivo);
            ImageIO.write(bufferedImage, sufixo, new File(arquivo));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void salvar(BufferedImage bufferedImage, File arquivo) {
        try {
            String sufixo = FileUtil.getFileExtension(arquivo.getPath());
            ImageIO.write(bufferedImage, sufixo, arquivo);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Image redimencionar(Image imagem, Integer width, Integer heigth) {
        Image img = imagem.getScaledInstance(width, heigth, Image.SCALE_DEFAULT);
        return img;
    }

    public static BufferedImage redimencionar(BufferedImage imagem, Integer width, Integer heigth) {
        Image img = imagem.getScaledInstance(width, heigth, Image.SCALE_DEFAULT);
        return imageParaBufferedImage(img, Boolean.FALSE);
    }

    public static BufferedImage recortar(BufferedImage bufferedImage, Integer x, Integer y, Integer w, Integer h) {
        FilteredImageSource filter = new FilteredImageSource(
                bufferedImage.getSource(), new CropImageFilter(x, y, w, h));
        Image imagem = Toolkit.getDefaultToolkit().createImage(filter);
        return criaBufferedImage(imagem);
    }

    public static Image bufferedImageParaImage(BufferedImage bufferedImage) {
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    //transparecia - true: tem, false: nao tem 
    public static BufferedImage imageParaBufferedImage(Image image, Boolean transparecia) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = transparecia;
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static byte[] imagemParaByte(File file) {
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            byte[] bytes = null;
            try {
                fileInputStream = new FileInputStream(file);
                int tamanho = (int) (file.length() + 1);
                bytes = new byte[tamanho];
                fileInputStream.read(bytes);
            } catch (IOException ex) {
                System.out.println(ex);
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException ex) {
                }
            }
            return bytes;
        } else {
            return null;
        }
    }

    public static BufferedImage byteParaImage(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            Image imagem = Toolkit.getDefaultToolkit().createImage(bytes);
            return imageParaBufferedImage(imagem, Boolean.TRUE);
        }
    }

    public static byte[] lerBytesImagem(InputStream in) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        }
    }

    public void receberSalvarImagemRede(ObjectInputStream entrada, String arquivo) {
        FileOutputStream file = null;
        try {
            byte[] bytes = new byte[1024];
            DataInputStream data = new DataInputStream(entrada);
            file = new FileOutputStream(arquivo);
            int leitura = data.read(bytes);
            while (leitura != -1) {
                if (leitura != -2) {
                    file.write(bytes, 0, leitura);
                }
                leitura = data.read(bytes);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
            }
        }
    }

    public void enviarLerImagemRede(ObjectOutputStream saida, String arquivo) {
        FileInputStream file = null;
        try {
            byte[] bytes = new byte[1024];
            file = new FileInputStream(arquivo);
            DataInputStream data = new DataInputStream(file);
            int leitura = data.read(bytes);
            while (leitura != -1) {
                if (leitura != -2) {
                    saida.write(bytes, 0, leitura);
                }
                leitura = data.read(bytes);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
            }
        }
    }

}
