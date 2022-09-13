package de.honoka.test.various.old.p2;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.junit.Test;

public class MIDITest {
	
	private interface MIDILibrary extends Library {
		MIDILibrary impl = Native.load("winmm.dll", MIDILibrary.class);
		int midiOutShortMsg(int handle, int message);
	}
	
	MIDILibrary midiLib = MIDILibrary.impl;
	
	private void send(int status, int channel, int note, int power) {
		midiLib.midiOutShortMsg(0,
				status | channel | (note << 8) | (power << 16));
	}
	
	private void playNote(int note) {
		try {
			send(0x90, 0, note, 100);
			Thread.sleep(100);
			send(0x80, 0, note, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void main() {
		for(int i = 60; i <= 72; i++) {
			playNote(i);
		}
	}
}
