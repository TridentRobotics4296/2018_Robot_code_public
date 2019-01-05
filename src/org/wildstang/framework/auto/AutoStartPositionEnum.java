/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.framework.auto;

/**
 *
 * @author coder65535
 */
public class AutoStartPositionEnum {

	private int index;
	private String description, configName;
	public static final int POSITION_COUNT = 7;// Remember to change when
												// defining new positions.

	public AutoStartPositionEnum(int index, String configName, String description) {
		this.configName = configName;
		this.index = index;
		this.description = description;
	}

	public static final AutoStartPositionEnum UNKNOWN = new AutoStartPositionEnum(0, "unknown", "Unknown Position");
	public static final AutoStartPositionEnum CENTER_BACK_PYRAMID = new AutoStartPositionEnum(1, "insidePyramidBackCenter", "Inside pyramid, back center");
	public static final AutoStartPositionEnum POSITION2 = new AutoStartPositionEnum(2, "unknown", "Unknown Position");
	public static final AutoStartPositionEnum BACK_LEFT_PYRAMID_INSIDE = new AutoStartPositionEnum(3, "insidePyramidBackLeft", "Inside pyramid, back left");
	public static final AutoStartPositionEnum BACK_RIGHT_PYRAMID_INSIDE = new AutoStartPositionEnum(4, "insidePyramidBackRight", "Inside Pyramid, back right");
	public static final AutoStartPositionEnum BACK_RIGHT_PYRAMID_OUTSIDE = new AutoStartPositionEnum(5, "outsidePyramidBackRight", "Outside Pyramid, back right");
	public static final AutoStartPositionEnum BACK_LEFT_PYRAMID_OUTSIDE = new AutoStartPositionEnum(6, "outsidePyramidBackLeft", "Outside Pyramid, back left");
	public static final AutoStartPositionEnum POSITION7 = new AutoStartPositionEnum(7, "unknown", "Unknown Position");
	public static final AutoStartPositionEnum POSITION8 = new AutoStartPositionEnum(8, "unknown", "Unknown Position");
	public static final AutoStartPositionEnum POSITION9 = new AutoStartPositionEnum(9, "unknown", "Unknown Position");

	/**
	 * Converts the enum type to a String.
	 *
	 * @return A string representing the enum.
	 */
	public String toString() {
		return description;
	}

	public String toConfigString() {
		return configName;
	}

	/**
	 * Converts the enum type to a numeric value.
	 *
	 * @return An integer representing the enum.
	 */
	public int toValue() {
		return index;
	}

	public static AutoStartPositionEnum getEnumFromValue(int i) {
		switch (i) {
		case 0:
			return AutoStartPositionEnum.UNKNOWN;
		case 1:
			return AutoStartPositionEnum.CENTER_BACK_PYRAMID;
		case 2:
			return AutoStartPositionEnum.POSITION2;
		case 3:
			return AutoStartPositionEnum.BACK_LEFT_PYRAMID_INSIDE;
		case 4:
			return AutoStartPositionEnum.BACK_RIGHT_PYRAMID_INSIDE;
		case 5:
			return AutoStartPositionEnum.BACK_RIGHT_PYRAMID_OUTSIDE;
		case 6:
			return AutoStartPositionEnum.BACK_LEFT_PYRAMID_OUTSIDE;
		case 7:
			return AutoStartPositionEnum.POSITION7;
		case 8:
			return AutoStartPositionEnum.POSITION8;
		case 9:
			return AutoStartPositionEnum.POSITION9;
		default:
			return null;
		}
	}
}
