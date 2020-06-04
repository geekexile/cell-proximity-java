package proximity;

import javax.microedition.rms.*;

import java.io.*;

public class OptionStorage {
	
	public String[] names = {"Human","Computer","Score","Medium"};
	
	private String Store = "ProximityOptions";
	
	public OptionStorage() {
		
		// Initialize / read scores from persistent storage.
		RecordStore recordStore = null;
		try {
			recordStore = RecordStore.openRecordStore(Store, true);
			
			// If the record store exists and contains records,
			// read the high scores.
			if (recordStore.getNumRecords() > 0) {
				for (int i = 0; i < names.length; i++) {
					byte[] record = recordStore.getRecord(i+1);
					DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record, 0, record.length));
					names[i] = istream.readUTF();
				}
			} else {
				// Otherwise, create the records and initialize them
				// with the default values. They will have record IDs
				// 1, 2, 3
				for (int i = 0; i < names.length; i++) {
					ByteArrayOutputStream bstream = new ByteArrayOutputStream(12);
					DataOutputStream ostream = new DataOutputStream(bstream);
					ostream.writeUTF(names[i]);
					ostream.flush();
					ostream.close();
					byte[] record = bstream.toByteArray();
					recordStore.addRecord(record, 0, record.length);
				}
			}
		} catch(Exception e) {
		} finally {
			if (recordStore != null) {
				try {
					recordStore.closeRecordStore();
				} catch(Exception e) {
				}
			}
		}
	}
	
	
	/**
	 * Updates the options database
	 */
	public void addOptions() {
		for (int i = 0; i < names.length; i++) {
			
			
			// Overwrite the scores in persistent storage.
			RecordStore recordStore = null;
			try {
				recordStore = RecordStore.openRecordStore(Store, true);
				for (int j = 0; j < names.length; j++) {
					ByteArrayOutputStream bstream = new ByteArrayOutputStream(12);
					DataOutputStream ostream = new DataOutputStream(bstream);
					ostream.writeUTF(names[j]);
					ostream.flush();
					ostream.close();
					byte[] record = bstream.toByteArray();
					recordStore.setRecord(j + 1, record, 0, record.length);
				}
			} catch(Exception e) {
			} finally {
				if (recordStore != null) {
					try {
						recordStore.closeRecordStore();
					} catch(Exception e) {
					}
				}
			}
			break;
		}
	}
}
