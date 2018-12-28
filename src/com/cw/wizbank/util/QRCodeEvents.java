package com.cw.wizbank.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeEvents {
	public void GenerateQR(String file_path, String content, String file_name)
			throws WriterException, IOException {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(content,
				BarcodeFormat.QR_CODE, 400, 400, hints);
		File file1 = new File(file_path, file_name);
		MatrixToImageWriter.writeToFile(bitMatrix, "png", file1);

	}
}
