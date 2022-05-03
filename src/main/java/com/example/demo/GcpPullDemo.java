package com.example.demo;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Storage;

@RestController
@RequestMapping("/gcp")
public class GcpPullDemo {

	@Autowired
	private Storage storage;

	@GetMapping("get-data")
	public String getData() throws IOException {
		StringBuffer sb = new StringBuffer();

		try (ReadChannel channel = storage.reader("file-storage-demo", "demofile.txt")) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(64 * 1024);
			while (channel.read(byteBuffer) > 0) {
				((Buffer) byteBuffer).flip();
				// byteBuffer.flip();
				String data = new String(byteBuffer.array(), 0, ((Buffer) byteBuffer).limit());
				sb.append(data);
				((Buffer) byteBuffer).clear();

			}
			return sb.toString();
		}
	}
}
