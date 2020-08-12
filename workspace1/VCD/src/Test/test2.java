package Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import org.jaudiotagger.audio.flac.*;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;


public class test2 {
	 public static void main(String[] agrs) throws Exception
	 {
		 play("C:\\Users\\WuJinPeng\\Desktop\\��ҵ\\java\\song\\Else\\G.E.M.������ - �������õ�ħ��.flac");
	 }
	 
	 
	 static void buff2Image(byte[] b,String tagSrc) throws Exception
	    {
	        FileOutputStream fout = new FileOutputStream(tagSrc);
	        //���ֽ�д���ļ�
	        fout.write(b);
	        fout.close();
	    }
	 
	 static byte[] image2Bytes(String imgSrc) throws Exception
	    {
	        FileInputStream fin = new FileInputStream(new File(imgSrc));
	        //�������,������Ͳ�����̫��,���̫���Ҫ������취������һ�δ���̶�����byte[]
	        byte[] bytes  = new byte[fin.available()];
	        //���ļ�����д���ֽ����飬�ṩ���Ե�case
	        fin.read(bytes);
	        fin.close();
	        return bytes;
	    }
	 
	 public static void play(String filePath) {
	        try {
	            // �ļ���
	            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
	            // �ļ�����
	            AudioFormat audioFormat = audioInputStream.getFormat();
	            System.out.println(audioFormat.getChannels());
	            AudioFormat a = new AudioFormat(Encoding.PCM_SIGNED, (float) 44100.0, 16, 2, 4, (float) 44100.0, false);
	            System.out.println(a);
	            //new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian)
	            // ת���ļ�����
	            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	                System.out.println(audioFormat.getEncoding());
	                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
	                // ��������Ҳת����ָ������
	                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
	            }
	            //audioFormat = a;
	            System.out.println(audioFormat);	            // ������豸
	            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
	            // ʹ�����еõ�һ�������豸
	            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	            // ����������ָ���ı����
	            sourceDataLine.open(audioFormat);
	            // ʹ�����еõ�����ʱ�Ϳ�ʼ����
	            sourceDataLine.start();
	 
	            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
	            // ����������д��������,��д�߲�
	            int numBytes = 1024 * bytesPerFrame;
	            byte[] audioBytes = new byte[numBytes];
	            while (audioInputStream.read(audioBytes) != -1) {
	                sourceDataLine.write(audioBytes, 0, audioBytes.length);
	            }
	            sourceDataLine.drain();
	            sourceDataLine.stop();
	            sourceDataLine.close();
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


}