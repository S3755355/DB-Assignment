package databaseSystemAss1;

import java.io.*;
import java.util.*;

public class dbload {

	public static void main(String[] args) {
		int pagesize = 0;
		String datafile = null;
		try {
			pagesize = Integer.parseInt(args[1]);
			datafile = args[2];
		} catch (Exception e) {
			System.out.println("Invalid arguments");
			System.exit(0);
		}
		long startTime, endTime, duration;

		startTime = System.currentTimeMillis();
		heapFile(pagesize, datafile);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println("Time taken for building heap page size " + pagesize + ": " + (duration / 1000) + "s");
	}

	public static void heapFile(int pagesize, String datafile) {
		File file = new File("heap" + "." + pagesize);
		int pageNum = 1;

		try {
			BufferedReader br = new BufferedReader(new FileReader(datafile));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

			String in;
			int page = 0;
			byte[] buffer = new byte[0];

			String[] header = (br.readLine()).split(",");
			int count = 0;
			while ((in = br.readLine()) != null) {
				String record = new String();

				String[] line = in.split(",");

				for (int i = 0; i < header.length; i++) {
					try {
						record = record.concat(header[i] + ":" + line[i]);

					} catch (Exception ie) {
						System.out.println(ie);
					}

					if (i == header.length - 1) {
						record = record.concat(",SDT_NAME:" + line[7] + line[1]);
						record = record.concat("\r\n");
					} else {
						record = record.concat(",");
					}
				}

				byte[] b = record.getBytes();

				if ((page + b.length) < pagesize) {
					buffer = concatByte(b, buffer);
					page += b.length;
				} else {
					byte[] temp = new byte[pagesize];
					System.arraycopy(buffer, 0, temp, 0, buffer.length);
					os.write(temp);
					System.out.print("Writing page " + pageNum + ". Previous page size is " + temp.length + "\r");
					buffer = new byte[0];
					concatByte(b, buffer);

					page = b.length;
					pageNum++;
				}

				
			}
			br.close();

			System.out.println();
			System.out.println("Finish loading.");
			System.out.println("Total page : " + pageNum);

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	
	public static byte[] concatByte(byte[] data, byte[] destination) {
		byte[] temp = new byte[destination.length + data.length];

		
		System.arraycopy(destination, 0, temp, 0, destination.length);
		System.arraycopy(data, 0, temp, destination.length, data.length);

		destination = temp;
		return destination;
	}
}