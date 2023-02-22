package io.github.dbstarll.utils.json.test;

import java.util.Arrays;

public class Model {
    private int intValue;

    private String stringValue;

    private boolean booleanValue;

    private float floatValue;

    private int[] intArray;

    public Model() {
    }

    /**
     * 构造Model.
     *
     * @param intValue     intValue
     * @param stringValue  stringValue
     * @param booleanValue booleanValue
     * @param floatValue   floatValue
     * @param intArray     intArray
     */
    public Model(int intValue, String stringValue, boolean booleanValue, float floatValue, int[] intArray) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
        this.floatValue = floatValue;
        this.intArray = intArray;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (booleanValue ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(floatValue);
        result = prime * result + Arrays.hashCode(intArray);
        result = prime * result + intValue;
        result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Model other = (Model) obj;
        if (booleanValue != other.booleanValue) {
            return false;
        }
        if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue)) {
            return false;
        }
        if (!Arrays.equals(intArray, other.intArray)) {
            return false;
        }
        if (intValue != other.intValue) {
            return false;
        }
        if (stringValue == null) {
            return other.stringValue == null;
        } else return stringValue.equals(other.stringValue);
    }

    @Override
    public String toString() {
        return "Model [intValue=" + intValue + ", stringValue=" + stringValue + ", booleanValue=" + booleanValue
                + ", floatValue=" + floatValue + ", intArray=" + Arrays.toString(intArray) + "]";
    }
}
