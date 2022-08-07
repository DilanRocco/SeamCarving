import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.Color;

// gets the color of a pixel
class ColorGetter implements Function<Pixel, Color> {
  public Color apply(Pixel t) {
    if (t.isRed) {
      return Color.red;
    }
    else {
      return t.color;
    }
  }
}

// produces a color representing the energy of the given pixel
class EnergyColor implements Function<Pixel, Color> {
  public Color apply(Pixel t) {
    int energy = (int) (t.energy * 255 / 6);
    // actual value is 4 * sqrt(2), we use 6 because it is the next integer above
    // 4sqrt2
    // this is so the max energy value is not above 255
    return new Color(energy, energy, energy);
  }
}

// produces a color representing the seam weight (totalWeight) of the given pixel
class SeamColor implements Function<Pixel, Color> {
  double scale;

  // scale represents the highest weight value
  SeamColor(double scale) {
    this.scale = scale;
  }

  // this default value is equivalent to what we were using before
  // we implemented max weight finding, so the checks we had before still work
  // yay backwards compatibility!
  SeamColor() {
    this(63.75);
  }

  public Color apply(Pixel t) {
    int weight = (int) ((t.seamInfo.totalWeight / scale) * 255);
    return new Color(weight, weight, weight);
  }
}

// Represents an iterator which iterates over a row of pixels
class GridRowIter implements Iterator<Pixel> {
  APixel curr;

  GridRowIter(APixel start) {
    this.curr = start;
  }

  // there is a next if the current APixel is a Pixel
  public boolean hasNext() {
    return curr instanceof Pixel;
  }

  // return the next pixel (the current pixel), and set the current pixel to the
  // next next
  public Pixel next() {
    if (!this.hasNext()) {
      throw new IllegalStateException("No next exists.");
    }

    Pixel next = (Pixel) this.curr;
    this.curr = this.curr.getRight();

    return next;
  }
}

// Represents an iterator which iterates over a column of pixels
class GridColIter implements Iterator<Pixel> {
  APixel curr;

  GridColIter(APixel start) {
    this.curr = start;
  }

  // there is a next if the current APixel is a Pixel
  public boolean hasNext() {
    return curr instanceof Pixel;
  }

  // return the next pixel (the current pixel), and set the current pixel to the
  // next next
  public Pixel next() {
    if (!this.hasNext()) {
      throw new IllegalStateException("No next exists.");
    }

    Pixel next = (Pixel) this.curr;
    this.curr = this.curr.getBot();

    return next;
  }
}

// represents the abstract pixel class
abstract class APixel {
  double brightness;
  double energy;

  APixel(double brightness, double energy) {
    this.brightness = brightness;
    this.energy = energy;
  }

  // get the pixel to the lef of this pixel
  abstract APixel getLeft();

  // get the pixel to the right of this pixel
  abstract APixel getRight();

  // get the pixel above of this pixel
  abstract APixel getTop();

  // get the pixel below this pixel
  abstract APixel getBot();

  // get the pixel to the top right of this pixel
  abstract APixel getTopRight();

  // default setters do nothing, this is intended
  // and overridden in the pixel class
  void setLeft(APixel p) {
    // default setter, do nothing
  }

  void setRight(APixel p) {
    // default setter, do nothing
  }

  void setUp(APixel p) {
    // default setter, do nothing
  }

  void setDown(APixel p) {
    // default setter, do nothing
  }

  // get the total weight of the seam associated with this pixel
  abstract double getTotalWeight();

  // get the SeamInfo associated with this pixel
  abstract SeamInfo getSeamInfo();

  // get the energy of this pixel
  double getEnergy() {
    return this.energy;
  }
}

// represents an edge pixel
class Edge extends APixel {
  Edge() {
    super(0.0, 0.0);
  }

  // all getters return this, because there will never be a case where the object
  // we want to get isnt an edge,
  // assuming out grid is well formed (which is ensured when creating the grid)
  // we ignore the one edge case where we ask for the pixel that this edge belongs
  // to (ex pixel.getLeft.getRight where the left is an edge) because this never
  // comes up and never will in our implementation, where once we get
  // to an edge we are done linkages with edges do not matter,
  // and can be effectively ignored or stated to be one way
  APixel getLeft() {
    return this;
  }

  APixel getRight() {
    return this;
  }

  APixel getTop() {
    return this;
  }

  APixel getBot() {
    return this;
  }

  APixel getTopRight() {
    return this;
  }

  // return max because the edge will never be part of a SeamInfo
  double getTotalWeight() {
    return Double.MAX_VALUE;
  }

  // return null because the SeamInfo of an edge is nonexistent
  SeamInfo getSeamInfo() {
    return null;
  }
}

// represents an individual pixel
class Pixel extends APixel {
  Color color;
  APixel left;
  APixel right;
  APixel up;
  APixel down;
  boolean isRed; // represents a boolean which determines if this pixel is colored red

  SeamInfo seamInfo;
  // we store seamInfo in a pixel and this pixel in the seam info in order to
  // use the graph of the pixels as the data structure for seamInfo.
  // this is logical, as using already existing infrastructure is better than
  // creating
  // an entire duplicate of the same structure for seamInfo

  Pixel(Color c, APixel left, APixel up) {
    super((c.getBlue() + c.getGreen() + c.getRed()) / 765.0, 0);
    // intialize brightness and energy
    this.color = c;
    this.left = left;
    this.up = up;

    this.isRed = false;

    this.right = new Edge();
    this.down = new Edge();

    this.seamInfo = new SeamInfo(this);
  }

  Pixel(Color c) {
    this(c, new Edge(), new Edge());
  }

  // EFFECT: sets this pixels left to the given pixel
  void setLeft(APixel left) {
    this.left = left;
  }

  // EFFECT: sets this pixels right to the given pixel
  void setRight(APixel right) {
    this.right = right;
  }

  // EFFECT: sets this pixels up to the given pixel
  void setUp(APixel up) {
    this.up = up;
  }

  // EFFECT: sets this pixels down to the given pixel
  void setDown(APixel down) {
    this.down = down;
  }

  // gets the left of this pixel
  APixel getLeft() {
    return this.left;
  }

  // gets the right of this pixel
  APixel getRight() {
    return this.right;
  }

  // gets the top of this pixel
  APixel getTop() {
    return this.up;
  }

  // gets the bottom of this pixel
  APixel getBot() {
    return this.down;
  }

  // gets the top left of this pixel
  APixel getTopLeft() {
    return this.up.getLeft();
  }

  // gets the top right of this pixel
  APixel getTopRight() {
    return this.up.getRight();
  }

  // gets the bottom left of this pixel
  APixel getBotLeft() {
    return this.down.getLeft();
  }

  // gets the bottom right of this pixel
  APixel getBotRight() {
    return this.down.getRight();
  }

  // gets the total weight of the SeamInfo associated with this pixel
  double getTotalWeight() {
    return this.seamInfo.totalWeight;
  }

  // get the SeamInfo associated with this pixel
  SeamInfo getSeamInfo() {
    return this.seamInfo;
  }

  // EFFECT: calculates and sets this pixels energy
  // this.energy is set to the proper value given the brightness of surrounding
  // pixels
  void calcEnergy() {
    double horzEnergy = (this.getTopLeft().brightness + 2 * this.getLeft().brightness
        + this.getBotLeft().brightness)
        - (this.getTopRight().brightness + 2 * this.getRight().brightness
            + this.getBotRight().brightness);

    double vertEnergy = (this.getTopLeft().brightness + 2 * this.getTop().brightness
        + this.getTopRight().brightness)
        - (this.getBotLeft().brightness + 2 * this.getBot().brightness
            + this.getBotRight().brightness);

    this.energy = Math.sqrt(Math.pow(horzEnergy, 2) + Math.pow(vertEnergy, 2));
  }
}

// represents a pixel grid
class PixelGrid {
  APixel topLeft; // top left is a Pixel, not an APixel, because it can never be an edge
  ArrayList<SeamInfo> removedSeams;
  int width;
  int height;

  PixelGrid(FromFileImage img) {
    this.removedSeams = new ArrayList<SeamInfo>();

    this.width = (int) img.getWidth();
    this.height = (int) img.getHeight();

    this.topLeft = new Pixel(img.getColorAt(0, 0)); // initialize the top left

    // construct the image
    APixel prev;
    APixel prevRow;
    APixel curr;

    prev = this.topLeft;
    prevRow = this.topLeft;

    // iterate over each row and column
    // this creates new pixels and links them properly
    for (int row = 0; row < height; row += 1) { // for each row
      for (int col = 1; col < width; col += 1) { // for each column
        curr = new Pixel(img.getColorAt(col, row));

        curr.setLeft(prev);
        curr.setUp(prev.getTopRight());

        prev.setRight(curr);
        prev.getTopRight().setDown(curr);

        prev = curr;
      }

      // if there is a next row, initialize the front of that row
      // this is the case for linking the first pixel in a row, the rest are handled
      // above
      if (row + 1 < height) {
        prev = new Pixel(img.getColorAt(0, row + 1));

        prev.setUp(prevRow);
        prevRow.setDown(prev);

        prevRow = prev;
      }
    }
  }

  // gets the pixel at a given x, y value
  // this returns a Pixel, not an APixel, as there are no edges inside the graph
  Pixel getPixel(int x, int y) {
    Iterator<Pixel> rowIter = new GridColIter(this.topLeft);
    Iterator<Pixel> colIter;

    // iterate to the correct row
    for (int row = y; row > 0; row -= 1) {
      rowIter.next();
    }

    // iterate to the correct col
    colIter = new GridRowIter(rowIter.next());

    for (int col = x; col > 0; col -= 1) {
      colIter.next();
    }

    // return the correct pixel
    return colIter.next();
  }

  // finds the minimum SeamInfo on the bottom row of the image
  // i.e. the minimum vertical seam
  SeamInfo findMinVerticalSeamInfo() {
    Pixel botRow = this.getPixel(0, this.height - 1);

    SeamInfo min = botRow.seamInfo;
    GridRowIter rowIter = new GridRowIter(botRow);

    while (rowIter.hasNext()) {
      Pixel next = rowIter.next();
      if (next.getTotalWeight() < min.totalWeight) {
        min = next.seamInfo;
      }
    }

    return min;
  }

  // finds the minimum SeamInfo on the right column of the image
  // i.e. the minimum horizontal seam
  SeamInfo findMinHorizontalSeamInfo() {
    Pixel rightCol = this.getPixel(this.width - 1, 0);

    SeamInfo min = rightCol.seamInfo;
    GridColIter colIter = new GridColIter(rightCol);

    while (colIter.hasNext()) {
      Pixel next = colIter.next();
      if (next.getTotalWeight() < min.totalWeight) {
        min = next.seamInfo;
      }
    }

    return min;
  }

  // finds the maximum weight of the seaminfo in this grid
  double findMaxSeamInfoWeight() {
    double max = this.topLeft.getTotalWeight();
    GridRowIter row = new GridRowIter(this.topLeft);
    GridColIter col;

    // iterate over each row and column to find each pixel
    // if the total weight of that pixel > max, set max to be that total weight
    // return the max
    while (row.hasNext()) {
      col = new GridColIter(row.next());
      while (col.hasNext()) {
        Pixel p = col.next();
        if (p.getTotalWeight() > max) {
          max = p.getTotalWeight();
        }
      }
    }

    return max;
  }

  // EFFECT: remove the minimum vertical seam from this PixelGrid,
  // update the direction to TRUE (vertical), add the removed seam to the list of
  // removed seams, and lower the width by one
  // ASSUME: energies and seam weights are already set
  void removeVerticalSeam() {
    SeamInfo minSeam = this.findMinVerticalSeamInfo();
    // if we are removing the topLeft Pixel, we must reassign topLeft to Right of
    // TopLeft
    if (minSeam.getLast() == this.topLeft) {
      this.topLeft = this.topLeft.getRight();
    }
    minSeam.removeVerticalSeam();
    minSeam.direction = true;
    this.removedSeams.add(minSeam);
    this.width -= 1;
  }

  // EFFECT: remove the minimum vertical seam from this PixelGrid,
  // update the direction to FALSE (horizontal), add the removed seam to the list
  // of removed seams, and lower the height by one
  // ASSUME: energies and seam weights are already set
  void removeHorizontalSeam() {
    SeamInfo minSeam = this.findMinHorizontalSeamInfo();

    // if we are removing the topLeft Pixel, we must reassign topLeft to Bottom of
    // TopLeft
    if (minSeam.getLast() == this.topLeft) {
      this.topLeft = this.topLeft.getBot();
    }
    minSeam.removeHorizontalSeam();
    minSeam.direction = false;
    this.removedSeams.add(minSeam);
    this.height -= 1;
  }

  // EFFECT: calculates and colors the minimum vertical seam
  void colorVerticalSeam() {
    this.calcEnergy();
    this.calcVerticalSeamInfo();
    SeamInfo minSeam = this.findMinVerticalSeamInfo();
    minSeam.colorSeam();
  }

  // EFFECT: calculates and colors the minimum horizontal seam
  void colorHorizontalSeam() {
    this.calcEnergy();
    this.calcHorizontalSeamInfo();
    SeamInfo minSeam = this.findMinHorizontalSeamInfo();
    minSeam.colorSeam();
  }

  // EFFECT: calculates the total energy for every pixel
  // by iterating through each pixel and calling .calcEnergy() on it
  void calcEnergy() {
    Iterator<Pixel> row = new GridColIter(this.topLeft);
    while (row.hasNext()) {
      Iterator<Pixel> col = new GridRowIter(row.next());
      while (col.hasNext()) {
        col.next().calcEnergy();
      }
    }
  }

  // EFFECT: calculates the seam info for every pixel
  // this calculates the vertical seam info, from the top to the bottom
  // hence, we start from the top left and go over each row from top to bottom,
  // calculating the vertical weight of each seaminfo successively.
  void calcVerticalSeamInfo() {
    Iterator<Pixel> row = new GridColIter(this.topLeft);
    while (row.hasNext()) {
      Iterator<Pixel> col = new GridRowIter(row.next());
      while (col.hasNext()) {
        Pixel next = col.next();
        next.seamInfo.calcVerticalWeight();
      }
    }
  }

  // EFFECT: calculates the seam info for every pixel
  // this calculates the horizontal seam info, from the left to the right
  // hence, we start from the top left and go over each column from left to right
  // calculating the horizontal weight of each seaminfo successively.
  void calcHorizontalSeamInfo() {
    Iterator<Pixel> col = new GridRowIter(this.topLeft);
    while (col.hasNext()) {
      Iterator<Pixel> row = new GridColIter(col.next());
      while (row.hasNext()) {
        Pixel next = row.next();
        next.seamInfo.calcHorizontalWeight();
      }
    }
  }

  // EFFECT: reinserts the last removed seam, if there is any
  void reinsertSeam() {
    if (this.removedSeams.size() > 0) {
      SeamInfo s = this.removedSeams.remove(this.removedSeams.size() - 1);
      // if the topleft was shifted at any point, this reshifts it back to normal
      if (this.topLeft.getLeft() instanceof Pixel) {

        this.topLeft = this.topLeft.getLeft();
      }
      if (this.topLeft.getTop() instanceof Pixel) {
        this.topLeft = this.topLeft.getTop();
      }
      // s.direction = true is a vertial seam
      if (s.direction) {
        this.width += 1;
      }
      else {
        this.height += 1;
      }

      s.reinsertSeam();
    }
  }

  // returns an image rendered using the given color function,
  // which takes a pixel and reuturns its color
  // this is the abstracted method used for the following render methods
  WorldImage renderGrid(Function<Pixel, Color> colorFunc) {
    // ACCUMULATOR: keeps track of the image result so far.
    // Order:Each element in the AL is a row.
    ComputedPixelImage res = new ComputedPixelImage(this.width, this.height);
    int x = 0; // x and y are accumulators representing the current x and y position
    int y = 0; // we do this so we can set the pixel's value

    Iterator<Pixel> row = new GridColIter(this.topLeft);
    Iterator<Pixel> col;

    while (row.hasNext()) {
      col = new GridRowIter(row.next());
      x = 0;

      while (col.hasNext()) {
        res.setPixel(x, y, colorFunc.apply(col.next()));
        x += 1;
      }

      y += 1;
    }

    return res;
  }

  // converts this PixelGrid into an image
  WorldImage renderImage() {
    return this.renderGrid(new ColorGetter());
  }

  // converts the PixelGrid to display the energy of the image
  WorldImage renderEnergy() {
    return this.renderGrid(new EnergyColor());
  }

  // converts this PixelGrid into an image of the SeamInfo weights
  WorldImage renderSeamInfo() {
    return this.renderGrid(new SeamColor());
  }

  // converts this PixelGrid into an image of the SeamInfo weights,
  // using the max seam info as a scale
  WorldImage renderSeamInfoMax() {
    return this.renderGrid(new SeamColor(this.findMaxSeamInfoWeight()));
  }
}

// represents seam info for a pixels
class SeamInfo {
  Pixel pixel;
  SeamInfo cameFrom;
  double totalWeight;
  boolean direction; // true is vertical

  SeamInfo(Pixel pixel, double totalWeight, SeamInfo cameFrom) {
    this.pixel = pixel;
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
    this.direction = true;
  }

  SeamInfo(Pixel pixel) {
    this(pixel, pixel.energy, null);
  }

  // EFFECT: set the total weight of this SeamInfo
  void setTotalWeight(double totalWeight) {
    this.totalWeight = totalWeight;

  }

  // EFFECT: calculates the weight of this SeamInfo
  void calcVerticalWeight() {
    APixel topLeft = pixel.getTopLeft();
    APixel top = pixel.getTop();
    APixel topRight = pixel.getTopRight();
    APixel min = topLeft;

    // find the min of the 3 pixels above this
    if (top.getTotalWeight() < min.getTotalWeight()) {
      min = top;
    }

    if (topRight.getTotalWeight() < min.getTotalWeight()) {
      min = topRight;
    }

    this.cameFrom = min.getSeamInfo(); // cameFrom will be null if min is an edge

    // update totalWeight if we came from a valid seam
    if (this.cameFrom != null) {
      this.totalWeight = this.pixel.getEnergy() + this.cameFrom.totalWeight;
      // fof is okay because we are in the SeamInfo class and this.camefrom is a
      // SeamInfo
    }
    else {
      this.totalWeight = this.pixel.getEnergy();
    }
  }

  // gets the last value of the SeamInfo
  Pixel getLast() {
    if (this.cameFrom == null) {
      return this.pixel;
    }
    else {
      return this.cameFrom.getLast();
    }
  }

  void calcHorizontalWeight() {
    APixel botLeft = pixel.getBotLeft(); // top right
    APixel left = pixel.getLeft(); // top
    APixel topLeft = pixel.getTopLeft();

    APixel min = botLeft;

    // find the min of the 3 pixels to the left of this
    if (left.getTotalWeight() < min.getTotalWeight()) {
      min = left;
    }

    if (topLeft.getTotalWeight() < min.getTotalWeight()) {
      min = topLeft;
    }

    this.cameFrom = min.getSeamInfo(); // cameFrom will be null if min is an edge

    // update totalWeight if we came from a valid seam
    if (this.cameFrom != null) {
      this.totalWeight = this.pixel.getEnergy() + this.cameFrom.totalWeight;
      // fof is okay because we are in the SeamInfo class and this.camefrom is a
      // SeamInfo
    }
    else {
      this.totalWeight = this.pixel.getEnergy();
    }
  }

  // EFFECT: removes this vertical seam
  // this seam must be at the bottom of the image in order for it to function
  // properly
  void removeVerticalSeam() {
    Pixel nextPixel = null;
    if (this.cameFrom != null) {
      nextPixel = this.cameFrom.pixel;
    }

    // link left and right
    this.pixel.getLeft().setRight(this.pixel.getRight());
    this.pixel.getRight().setLeft(this.pixel.getLeft());

    // this is the termination case
    if (nextPixel == null) {
      // check if the next pixel exists
      // if it doesnt, we have reached the edge and are done.
      this.pixel.setUp(new Edge());
      return;
    }

    // cases for linking diagonals when the next pixel is to the top left or top
    // right
    if (nextPixel == this.pixel.getTopLeft()) {
      this.pixel.getTop().setDown(this.pixel.getLeft());
      this.pixel.getLeft().setUp(this.pixel.getTop());
    }
    else if (nextPixel == this.pixel.getTopRight()) {
      this.pixel.getTop().setDown(this.pixel.getRight());
      this.pixel.getRight().setUp(this.pixel.getTop());
    }

    this.cameFrom.removeVerticalSeam();
  }

  // EFFECT: removes this horizontal seam
  // this seam must originate at the rightmost side of the image
  // in order for it to function properly
  void removeHorizontalSeam() {
    Pixel nextPixel = null;
    if (this.cameFrom != null) {
      nextPixel = this.cameFrom.pixel;
    }

    // link left and right
    this.pixel.getTop().setDown(this.pixel.getBot());
    this.pixel.getBot().setUp(this.pixel.getTop());

    // this is the termination case
    if (nextPixel == null) {
      // check if the next pixel exists
      // if it doesnt, we have reached the edge and are done.
      this.pixel.setLeft(new Edge());
      return;
    }

    // cases for linking diagonals when the next pixel is to the bottom left or top
    // left
    if (nextPixel == this.pixel.getBotLeft()) {
      this.pixel.getLeft().setRight(this.pixel.getBot());
      this.pixel.getBot().setLeft(this.pixel.getLeft());
    }
    else if (nextPixel == this.pixel.getTopLeft()) {
      this.pixel.getLeft().setRight(this.pixel.getTop());
      this.pixel.getTop().setLeft(this.pixel.getLeft());
    }

    this.cameFrom.removeHorizontalSeam();
  }

  // EFFECT: colors all pixels of this seam red
  void colorSeam() {
    this.pixel.isRed = true;
    if (this.cameFrom != null) {
      this.cameFrom.colorSeam();
    }
  }

  // EFFECT: reinserts this seam into the image
  void reinsertSeam() {
    this.pixel.getLeft().setRight(this.pixel);
    this.pixel.getRight().setLeft(this.pixel);
    this.pixel.getTop().setDown(this.pixel);
    this.pixel.getBot().setUp(this.pixel);

    this.pixel.isRed = false;

    if (this.cameFrom != null) {
      this.cameFrom.reinsertSeam();
    }
  }
}

// represents a Big Bang world for seam carving
class SeamCarving extends World {
  String img;
  PixelGrid grid;
  boolean remove; // if true, remove the seam. else, color it
  boolean vertical; // are we removing a vertical or horizontal seam?
  boolean running; // determines if the seamcarving is running
  int renderType; // 1 = image, 2 = energy, 3 = seaminfo

  SeamCarving(String img) {
    this.img = img;
    this.grid = new PixelGrid(new FromFileImage(img));
    this.remove = false;
    this.vertical = true;
    this.running = false;
    this.renderType = 1;
  }

  // makes the scene of the seam carving
  public WorldScene makeScene() {
    WorldImage scene = new EmptyImage();

    if (this.renderType == 1) {
      scene = this.grid.renderImage();
    }
    else if (this.renderType == 2) {
      scene = this.grid.renderEnergy();
    }
    else if (this.renderType == 3) {
      scene = this.grid.renderSeamInfoMax();
    }

    // grid is a PixelGrid and all PixelGrids have width and height
    // these are values which we will always want to access
    WorldScene w = new WorldScene(this.grid.width, this.grid.height);
    w.placeImageXY(scene, this.grid.width / 2, this.grid.height / 2);
    return w;
  }

  // EFFECT: randomly sets the direction
  void setRandomDirection() {
    // one way of doing random selection which preserves the aspect ratio of the
    // given image
    // this.vertical = Math.random() < (double) this.grid.width/(this.grid.width +
    // this.grid.height);
    this.vertical = true;
  }

  // EFFECT: represents the key handler and updates the state based on key presses
  public void onKeyEvent(String key) {

    if (key.equals("r") && !this.running && !this.remove) {
      if (this.remove) {
        this.grid.removeVerticalSeam();
      }

      this.grid.reinsertSeam();
    }
    else if (key.equals(" ")) {
      this.running = !this.running;
    }
    else if (key.equals("1")) {
      this.renderType = 1;
    }
    else if (key.equals("2")) {
      this.renderType = 2;
    }
    else if (key.equals("3")) {
      this.renderType = 3;
    }
    else if (key.equals("v") && !this.running && !this.remove && this.grid.width > 1) {

      this.grid.colorVerticalSeam();
      this.grid.removeVerticalSeam();
      this.running = false;
    }
    else if (key.equals("h") && !this.running && !this.remove && this.grid.height > 1) {

      this.grid.colorHorizontalSeam();
      this.grid.removeHorizontalSeam();
      this.running = false;
    }
  }

  // EFFECT: removes a seam every tick if it is possible and running
  public void onTick() {
    if (running) {
      if (this.remove) {
        if (this.vertical && this.grid.width > 1) {
          this.grid.removeVerticalSeam();
        }
        else if (this.grid.height > 1) {
          this.grid.removeHorizontalSeam();
        }
        this.remove = !this.remove;
      }
      else {
        this.setRandomDirection();
        if (this.vertical && this.grid.width > 1) {
          this.grid.colorVerticalSeam();
        }
        else if (this.grid.height > 1) {
          this.grid.colorHorizontalSeam();
        }
        this.remove = !this.remove;
      }
    }
  }
}
