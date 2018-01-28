package utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.io.IOUtils;

public class FileCompress {
	
	private final static int BUFFER_SIZE = 4096;
	
	public static byte[] fileToBytes(File file) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        return IOUtils.toByteArray(inputStream);
    }

    public static void bytesToFile(byte[] data, File filename) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(data);
        fos.close();
    }

    public static byte[] createTar(File[] files) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        TarArchiveOutputStream out = new TarArchiveOutputStream(new BufferedOutputStream(bos));
        for (File f : files) {
            byte[] data = FileCompress.fileToBytes(f);
            if (f.getName() != null && data != null) {
                TarArchiveEntry entry = new TarArchiveEntry(f.getName());

                entry.setSize(data.length);
                out.putArchiveEntry(entry);
                out.write(data);
                out.closeArchiveEntry();
            }
        }
        out.close();

        return bos.toByteArray();
    }

    public static byte[] createXz(File tarFile) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(tarFile);        
        XZCompressorOutputStream xzOut = new XZCompressorOutputStream(bos);
        
        final byte[] buffer = new byte[BUFFER_SIZE];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            xzOut.write(buffer, 0, n);
        }
        
        xzOut.close();
        in.close();

        return bos.toByteArray();
    }

    public static byte[] createXz(byte[] buffer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        XZCompressorOutputStream xzOut = new XZCompressorOutputStream(bos);
        xzOut.write(buffer, 0, buffer.length);
        xzOut.close();

        return bos.toByteArray();
    }
        
    public static void createTarXz(File directory, File outputPkg) throws Exception {
    	
    	byte[] tarBytes = FileCompress.createTar(directory.listFiles());
    	byte[] tarXzBytes = FileCompress.createXz(tarBytes);
    	FileCompress.bytesToFile(tarXzBytes, outputPkg);
    }
	
}
