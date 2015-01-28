/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.tools;

import java.nio.ByteBuffer;

/**
 * @author Seth
 *
 */
public class PrintData
{
	public static String printData(byte[] data, int len)
	{
		final StringBuilder result = new StringBuilder(len * 4);
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				if(i > 500)
					break;
				
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = 0xFF & data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = 0xFF & data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}
	
	public static String printData(long[] data, int len)
	{
		final StringBuilder result = new StringBuilder(len * 4);
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				if(i > 500)
					break;
				
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					long t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				long t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}
	
	public static String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i = number.length(); i < digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
	
	public static String fillHex(long data, int digits)
	{
		String number = Long.toHexString(data);
		
		for (int i = number.length(); i < digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
	
	/**
	 * @param raw
	 * @return
	 */
	public static String printData(byte[] raw)
	{
		return printData(raw, raw.length);
	}
	
	public static String printData(ByteBuffer buf)
	{
		byte[] data = new byte[buf.remaining()];
		buf.get(data);
		String hex = printData(data, data.length);
		buf.position(buf.position() - data.length);
		return hex;
	}

	/**
	 * @param data
	 * @return 
	 */
	public static String printData(short[] data)
	{
		int len = data.length;
		final StringBuilder result = new StringBuilder(len * 4);
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				if(i > 500)
					break;
				
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = 0xFF & data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = 0xFF & data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}

	public static String printData(long[] data)
	{
	
		return printData(data, data.length);
	}
	
	/**
	 * @param data
	 * @return 
	 */
	public static String printData(int[] data)
	{
		int len = data.length;
		final StringBuilder result = new StringBuilder(len * 4);
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				if(i > 500)
					break;
				
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = 0xFF & data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = 0xFF & data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}

	public static String printData(char[] data)
	{
		int len = data.length;
		final StringBuilder result = new StringBuilder(len * 4);
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				if(i > 500)
					break;
				
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = 0xFF & data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = 0xFF & data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}
}
