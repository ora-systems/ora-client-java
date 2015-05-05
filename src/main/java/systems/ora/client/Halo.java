package systems.ora.client;

import java.security.InvalidParameterException;

class Halo {
  private int appId;
  private int id = 0;
  private double size = 0.0;
  private double complexity = 0.0;
  private double brightness = 0.0;
  private double wobble = 0.0;
  private double color = 0.0;
  private double speed = 0.0;

  /**
   * Create an empty halo object.
   */
  public Halo(int iAppId) {
    appId = iAppId;
  }

  /**
   * Create a halo object with all settings pre-set.
   */
  public Halo(int iAppId, 
      double iSize, double iColor, double iComplexity, double iSpeed, double iBrightness, double iWobble) 
      throws InvalidParameterException 
  {
    appId = iAppId;

    validate(iSize);
    validate(iColor);
    validate(iComplexity);
    validate(iSpeed);
    validate(iBrightness);
    validate(iWobble);

    size       = iSize;
    color      = iColor;
    complexity = iComplexity;
    speed      = iSpeed;
    brightness = iBrightness;
    wobble     = iWobble;
  }

  /**
   * Provides a displayable string of halo information.
   */
  public String toString() {
    return "haloId: " + Integer.toString(id) +
        "\nappId: " + Integer.toString(appId) +
        "\nsize: " + Double.toString(size) +
        "\ncolor: " + Double.toString(color) +
        "\ncomplexity: " + Double.toString(complexity) +
        "\nspeed: " + Double.toString(speed) +
        "\nbrightness: " + Double.toString(brightness) +
        "\nwobble: " + Double.toString(wobble) + "\n";
  }

  public int getId() {
    return id;
  }

  public double getSize() {
    return size;
  }

  public double getColor() {
    return color;
  }

  public double getComplexity() {
    return complexity;
  }

  public double getSpeed() {
    return speed;
  }

  public double getBrightness() {
    return brightness;
  }

  public double getWobble() {
    return wobble;
  }

  public void setId(int haloId) {
    id = haloId;
  }

  public void setSize(double s) throws InvalidParameterException {
    validate(s);
    size = s;
  }

  public void setColor(double c) throws InvalidParameterException {
    validate(c);
    color = c;
  }

  public void setComplexity(double c) throws InvalidParameterException {
    validate(c);
    complexity = c;
  }

  public void setSpeed(double s) throws InvalidParameterException {
    validate(s);
    speed = s;
  }

  public void setBrightness(double b) throws InvalidParameterException {
    validate(b);
    brightness = b;
  }

  public void setWobble(double w) throws InvalidParameterException {
    validate(w);
    wobble = w;
  }

  private void validate(double val) throws InvalidParameterException {
    if (val < 0.0 || val > 1.0) throw new InvalidParameterException();
  }
}