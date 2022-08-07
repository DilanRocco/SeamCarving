import tester.*;
import java.util.ArrayList;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;


class ExamplesSeamCarving {
  FromFileImage diagImage = new FromFileImage("src/resources/5x5_diag_rendered.jpg");
  FromFileImage diagEnergy = new FromFileImage("src/resources/5x5_diag_energy.jpg");
  FromFileImage diagSeamInfo = new FromFileImage("src/resources/5x5_diag_seaminfo.jpg");
  FromFileImage diagRendered = new FromFileImage("src/resources/5x5_diag_rendered.jpg");
  FromFileImage diagSeamInfoMax = new FromFileImage("src/resources/diagSeamInfoMax.jpg");

  FromFileImage straightCenterImage = new FromFileImage("src/resources/5x5_straight_center.jpg");
  FromFileImage straightEnergy = new FromFileImage("src/resources/5x5_straight_energy.jpg");
  FromFileImage straightSeamInfo = new FromFileImage("src/resources/5x5_straight_seaminfo.jpg");
  FromFileImage straightRendered = new FromFileImage("src/resources/5x5_straight_rendered.jpg");
  FromFileImage straightSeamInfoMax = new FromFileImage("src/resources/straightSeamInfoMax.jpg");

  FromFileImage crossImage = new FromFileImage("src/resources/5x5_x.jpg");
  FromFileImage crossEnergy = new FromFileImage("src/resources/5x5_x_energy.jpg");
  FromFileImage crossSeamInfo = new FromFileImage("src/resources/5x5_x_seaminfo.jpg");
  FromFileImage crossRendered = new FromFileImage("src/resources/5x5_x_rendered.jpg");
  FromFileImage crossSeamInfoMax = new FromFileImage("src/resources/crossSeamInfoMax.jpg");

  FromFileImage wideSeam0 = new FromFileImage("src/resources/wide_seam_0.jpg");
  FromFileImage wideSeam1 = new FromFileImage("src/resources/wide_seam_1.jpg");

  FromFileImage g1_remove = new FromFileImage("src/resources/g3_remove_1.jpg");
  FromFileImage g2_remove = new FromFileImage("src/resources/g3_remove_2.jpg");
  FromFileImage g3_remove = new FromFileImage("src/resources/g3_remove_3.jpg");

  FromFileImage g3_remove_hor1 = new FromFileImage("src/resources/g3_remove_1_hor.jpg");
  FromFileImage g3_remove_hor2 = new FromFileImage("src/resources/g3_remove_2_hor.jpg");
  FromFileImage g3_remove_hor3 = new FromFileImage("src/resources/g3_remove_3_hor.jpg");

  PixelGrid diagGrid = new PixelGrid(diagImage);
  PixelGrid straightGrid = new PixelGrid(straightCenterImage);
  PixelGrid crossGrid = new PixelGrid(crossImage);

  PixelGrid g1_removeGrid = new PixelGrid(g1_remove);
  PixelGrid g2_removeGrid = new PixelGrid(g2_remove);
  PixelGrid g3_removeGrid = new PixelGrid(g3_remove);

  PixelGrid g3_removeGrid_hor1 = new PixelGrid(g3_remove_hor1);
  PixelGrid g3_removeGrid_hor2 = new PixelGrid(g3_remove_hor2);
  PixelGrid g3_removeGrid_hor3 = new PixelGrid(g3_remove_hor3);

  PixelGrid wideSeamGrid = new PixelGrid(wideSeam0);
  PixelGrid wideSeam1Grid = new PixelGrid(wideSeam1);

  Edge edge = new Edge();
  Pixel pixel1;
  Pixel pixel2;
  Pixel pixel3;
  Pixel pixel4;
  Pixel pixel5;
  Pixel pixel6;

  Pixel pixel7;
  Pixel pixel8;
  Pixel pixel9;
  Pixel pixel10;

  Pixel pixel11;
  Pixel pixel12;
  Pixel pixel13;
  Pixel pixel14;

  // 3x3 Image
  FromFileImage img1 = new FromFileImage("src/resources/example1.jpg");
  PixelGrid g1;

  // Balloons Image
  FromFileImage img2 = new FromFileImage("src/resources/balloons.jpg");
  
  PixelGrid g2;

  // random image
  FromFileImage img3 = new FromFileImage("src/resources/4x4ran.jpg");
  PixelGrid g3;

  ArrayList<Pixel> pixels1;
  ArrayList<Pixel> pixels2;

  SeamInfo sm1;
  SeamInfo sm2;
  SeamInfo sm3;
  SeamInfo sm4;
  SeamInfo sm5;

  void initData() {
    diagGrid = new PixelGrid(diagImage);
    straightGrid = new PixelGrid(straightCenterImage);
    crossGrid = new PixelGrid(crossImage);
    wideSeamGrid = new PixelGrid(wideSeam0);
    pixel1 = new Pixel(Color.blue);
    pixel2 = new Pixel(Color.green);
    pixel3 = new Pixel(Color.orange);
    pixel4 = new Pixel(Color.pink);
    pixel5 = new Pixel(Color.gray);

    pixel1.setDown(pixel2);
    pixel1.setLeft(pixel2);
    pixel1.calcEnergy();

    pixel2.setRight(pixel5);

    pixel3.setRight(pixel1);
    pixel3.setLeft(pixel2);
    pixel3.setUp(pixel2);

    pixel3.calcEnergy();

    pixel4.setDown(pixel1);
    pixel4.setUp(pixel1);
    pixel4.setRight(pixel1);
    pixel4.setLeft(pixel1);

    pixel5.setLeft(edge);

    pixel6 = new Pixel(Color.gray);

    ArrayList<Pixel> pixels1 = new ArrayList<Pixel>();
    pixel7 = new Pixel(Color.orange);
    pixel8 = new Pixel(Color.orange);
    pixel9 = new Pixel(Color.orange);
    pixel10 = new Pixel(Color.orange);
    pixels1.add(pixel7);
    pixels1.add(pixel8);
    pixels1.add(pixel9);
    pixels1.add(pixel10);

    pixel11 = new Pixel(Color.orange);
    pixel12 = new Pixel(Color.orange);
    pixel13 = new Pixel(Color.orange);
    pixel14 = new Pixel(Color.orange);

    g1 = new PixelGrid(img1);
    g2 = new PixelGrid(img2);
    g3 = new PixelGrid(img3);

    sm1 = new SeamInfo(pixel1);
    sm2 = new SeamInfo(pixel2);
    sm3 = new SeamInfo(pixel2);
    sm4 = new SeamInfo(g2.getPixel(0, 0));
    sm5 = new SeamInfo(g2.getPixel(0, 2));
    // initPictures();
  }
//   void testInit(Tester t) {
//     initData();
//   }
  // checks if two images are equal by placing them on a scene and comparing the
  // resulting scene
  boolean imageEqual(WorldImage img1, WorldImage img2) {
    WorldScene s0;
    WorldScene s1;

    if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
      return false;
    }

    s0 = new WorldScene((int) img1.getWidth(), (int) img1.getHeight());
    s1 = new WorldScene((int) img1.getWidth(), (int) img1.getHeight());

    s0.placeImageXY(img1, 0, 0);
    s1.placeImageXY(img2, 0, 0);

    return s0.equals(s1);
  }

  void testImageEqual(Tester t) {
    this.initData();
    t.checkExpect(this.imageEqual(img1, img2), false);
    t.checkExpect(this.imageEqual(img1, img1), true);
    t.checkExpect(this.imageEqual(diagImage, img1), false);
    t.checkExpect(this.imageEqual(diagImage, diagImage), true);
  }

  // tests all render methods and the main calculation methods
  // (calcVerticalSeamInfo and
  // calcEnergy)
  void testCalcRender(Tester t) {
    this.initData();
    t.checkExpect(this.imageEqual(crossGrid.renderImage(), crossImage), true);
    t.checkExpect(this.imageEqual(diagGrid.renderEnergy(), diagEnergy), true);
    t.checkExpect(this.imageEqual(diagGrid.renderSeamInfo(), diagSeamInfo), true);
    diagGrid.calcEnergy();
    diagGrid.calcVerticalSeamInfo();
    t.checkExpect(this.imageEqual(diagGrid.renderSeamInfoMax(), diagSeamInfoMax), true);

    t.checkExpect(this.imageEqual(straightGrid.renderImage(), straightCenterImage), true);
    t.checkExpect(this.imageEqual(straightGrid.renderEnergy(), straightEnergy), true);
    t.checkExpect(this.imageEqual(straightGrid.renderSeamInfo(), straightSeamInfo), true);
    straightGrid.calcEnergy();
    straightGrid.calcVerticalSeamInfo();
    t.checkExpect(this.imageEqual(straightGrid.renderSeamInfoMax(), straightSeamInfoMax), true);

    t.checkExpect(this.imageEqual(crossGrid.renderImage(), crossImage), true);
    t.checkExpect(this.imageEqual(crossGrid.renderEnergy(), crossEnergy), true);
    t.checkExpect(this.imageEqual(crossGrid.renderSeamInfo(), crossSeamInfo), true);
    crossGrid.calcEnergy();
    crossGrid.calcVerticalSeamInfo();
    t.checkExpect(this.imageEqual(crossGrid.renderSeamInfoMax(), crossSeamInfoMax), true);
  }

  void testGetPixel(Tester t) {
    this.initData();
    t.checkExpect(crossGrid.getPixel(0, 0), crossGrid.topLeft);
    t.checkExpect(crossGrid.getPixel(1, 0), crossGrid.topLeft.getRight());
    t.checkExpect(crossGrid.getPixel(0, 1), crossGrid.topLeft.getBot());
  }

  void testGetLeft(Tester t) {
    initData();
    t.checkExpect(edge.getLeft(), edge);
    t.checkExpect(pixel4.getLeft(), pixel1);
    t.checkExpect(pixel3.getBot(), edge);
    t.checkExpect(pixel1.getLeft(), pixel2);
  }

  void testGetRight(Tester t) {
    initData();
    t.checkExpect(edge.getRight(), edge);
    t.checkExpect(pixel4.getRight(), pixel1);

  }

  void testGetBot(Tester t) {
    initData();
    t.checkExpect(edge.getBot(), edge);
    t.checkExpect(pixel1.getBot(), pixel2);
  }

  void testGetTop(Tester t) {
    initData();
    t.checkExpect(edge.getTop(), edge);
    t.checkExpect(pixel3.getTop(), pixel2);
  }

  void testSetLeft(Tester t) {
    initData();
    pixel6.setLeft(edge);
    t.checkExpect(pixel6.getLeft(), edge);
    pixel6.setLeft(pixel5);
    t.checkExpect(pixel6.getLeft(), pixel5);
    edge.setLeft(edge);
    t.checkExpect(edge.getLeft(), edge);
  }

  void testSetRight(Tester t) {
    initData();
    pixel6.setRight(edge);
    t.checkExpect(pixel6.getRight(), edge);
    pixel6.setRight(pixel5);
    t.checkExpect(pixel6.getRight(), pixel5);
    edge.setRight(edge);
    t.checkExpect(edge.getRight(), edge);
  }

  void testSetDown(Tester t) {
    initData();
    pixel6.setDown(edge);
    t.checkExpect(pixel6.getBot(), edge);
    pixel6.setDown(pixel5);
    t.checkExpect(pixel6.getBot(), pixel5);
    edge.setDown(edge);
    t.checkExpect(edge.getBot(), edge);
  }

  void testSetUp(Tester t) {
    initData();
    pixel6.setUp(edge);
    t.checkExpect(pixel6.getTop(), edge);
    pixel6.setUp(pixel5);
    t.checkExpect(pixel6.getTop(), pixel5);
    edge.setUp(edge);
    t.checkExpect(edge.getTop(), edge);
  }

  void testGetTotalWeight(Tester t) {
    initData();
    // make seam was never called so the weight is the default value of zero
    t.checkExpect(pixel5.getTotalWeight(), 0.0);
    g1.colorVerticalSeam(); // we must call colorVerticalSeam to calculate the total weight
    t.checkInexact(g1.getPixel(0, 0).getTotalWeight(), 1.257, 0.001);
    t.checkInexact(g1.getPixel(2, 1).getTotalWeight(), 2.466, 0.001);
    g2.colorVerticalSeam();
    t.checkInexact(g2.getPixel(50, 140).getTotalWeight(), 4.014, 0.001);
    t.checkExpect(edge.getTotalWeight(), Double.MAX_VALUE);
  }

  void testGetSeamInfo(Tester t) {
    initData();
    // An edge's seamInfo is always null
    t.checkExpect(edge.getSeamInfo(), null);
    t.checkExpect(pixel1.getSeamInfo(), new SeamInfo(pixel1, 0, null));
    t.checkInexact(g1.getPixel(0, 0).getSeamInfo(), new SeamInfo(g1.getPixel(0, 0)), 0.01);
    t.checkInexact(g1.getPixel(1, 0).getSeamInfo(), new SeamInfo(g1.getPixel(1, 0)), 0.01);
    t.checkInexact(g1.getPixel(2, 0).getSeamInfo(), new SeamInfo(g1.getPixel(2, 0)), 0.01);
  }

  void testGetTopLeft(Tester t) {
    initData();
    t.checkExpect(pixel2.getTopLeft(), edge);
    t.checkExpect(pixel4.getTopLeft(), pixel2);
  }

  void testGetTopRight(Tester t) {
    initData();
    t.checkExpect(edge.getTopRight(), edge);
    t.checkExpect(pixel2.getTopRight(), edge);
    t.checkExpect(pixel3.getTopRight(), pixel5);
  }

  void testGetBotLeft(Tester t) {
    initData();
    t.checkExpect(pixel3.getBotLeft(), edge);
    t.checkExpect(pixel4.getBotLeft(), pixel2);
  }

  void testGetBotRight(Tester t) {
    initData();
    t.checkExpect(pixel1.getBotRight(), pixel5);
    t.checkExpect(pixel2.getBotRight(), edge);
  }

  void testCalcEnergy(Tester t) {
    initData();
    t.checkInexact(pixel1.energy, 0.9428, .001);
    t.checkInexact(pixel2.energy, 0.0, .001);
    t.checkInexact(pixel3.energy, 1.271, .001);
    g2.calcEnergy();
    t.checkInexact(g2.getPixel(4, 4).energy, 0.0149, .001);

  }

  void testSetTotalWeight(Tester t) {
    initData();
    sm1.setTotalWeight(30.0);
    t.checkExpect(sm1.totalWeight, 30.0);
    sm2.setTotalWeight(10.1);
    t.checkExpect(sm2.totalWeight, 10.1);
    sm2.setTotalWeight(9.1);
    t.checkExpect(sm2.totalWeight, 9.1);
  }

  void testCalcVerticalWeight(Tester t) {
    initData();
    sm1.colorSeam();
    t.checkInexact(sm1.totalWeight, .9428, .001);
    // calcVerticalWeight is not called so it is the default value of 0.0
    t.checkInexact(sm2.totalWeight, 0.0, .001);
  }

  void testColorGetter(Tester t) {
    initData();
    t.checkExpect(new ColorGetter().apply(pixel1), Color.blue);
    t.checkExpect(new ColorGetter().apply(pixel2), Color.green);
    pixel2.isRed = true;
    t.checkExpect(new ColorGetter().apply(pixel2), Color.red);
  }

  void testEnergyColor(Tester t) {
    initData();
    t.checkExpect(new EnergyColor().apply(pixel1), new Color(40, 40, 40));
    // must calculate the energy first because it's defaulted to zero
    t.checkExpect(new EnergyColor().apply(g1.getPixel(0, 0)), new Color(0, 0, 0));
    g1.calcEnergy();
    t.checkExpect(new EnergyColor().apply(g1.getPixel(0, 0)), new Color(53, 53, 53));
  }

  void testSeamColor(Tester t) {
    initData();
    // must call colorVerticalSeam first because the color defaults to zero
    t.checkExpect(new SeamColor().apply(g1.getPixel(0, 0)), new Color(0, 0, 0));
    g1.colorVerticalSeam();
    t.checkExpect(new SeamColor().apply(g1.getPixel(0, 0)), new Color(5, 5, 5));
  }

  void testGridRowIter(Tester t) {
    initData();
    GridRowIter iter1 = new GridRowIter(g1.getPixel(0, 0));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(0, 0));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(1, 0));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(2, 0));
    t.checkExpect(iter1.hasNext(), false);

    GridRowIter iter2 = new GridRowIter(g2.getPixel(0, 0));
    t.checkExpect(iter2.hasNext(), true);
    t.checkExpect(iter2.next(), g2.getPixel(0, 0));
    for (int i = 0; i < g2.width - 1; i += 1) {
      iter2.next();
    }
    t.checkExpect(iter2.hasNext(), false);
  }

  void testGridColIter(Tester t) {
    initData();
    GridColIter iter1 = new GridColIter(g1.getPixel(0, 0));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(0, 0));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(0, 1));
    t.checkExpect(iter1.hasNext(), true);
    t.checkExpect(iter1.next(), g1.getPixel(0, 2));
    t.checkExpect(iter1.hasNext(), false);

    GridColIter iter2 = new GridColIter(g2.getPixel(0, 0));
    t.checkExpect(iter2.hasNext(), true);
    t.checkExpect(iter2.next(), g2.getPixel(0, 0));
    for (int i = 0; i < g2.height - 1; i += 1) {
      iter2.next();
    }
    t.checkExpect(iter2.hasNext(), false);
  }

  void testFindMinVerticalSeamInfo(Tester t) {
    initData();
    t.checkExpect(g1.findMinVerticalSeamInfo(), g1.getPixel(0, 2).seamInfo);
    t.checkExpect(g3.findMinVerticalSeamInfo(), g3.getPixel(0, 3).seamInfo);
    t.checkExpect(diagGrid.findMinVerticalSeamInfo(), diagGrid.getPixel(0, 4).seamInfo);
  }

  void testFindMinHorizontalSeamInfo(Tester t) {
    initData();
    t.checkExpect(g1.findMinHorizontalSeamInfo(), g1.getPixel(2, 0).seamInfo);
    t.checkExpect(g3.findMinHorizontalSeamInfo(), g3.getPixel(3, 0).seamInfo);
    t.checkExpect(diagGrid.findMinHorizontalSeamInfo(), diagGrid.getPixel(4, 0).seamInfo);
  }

  void testFindMaxSeamInfoWeight(Tester t) {
    initData();
    // first is zero because we don't initalize anything
    t.checkExpect(g1.findMaxSeamInfoWeight(), 0.0);
    g1.calcEnergy();
    g1.calcHorizontalSeamInfo();
    t.checkInexact(g1.findMaxSeamInfoWeight(), 3.434, 0.01);
    g3.calcEnergy();
    g3.calcHorizontalSeamInfo();
    t.checkInexact(g3.findMaxSeamInfoWeight(), 4.085, 0.01);
    g2.calcEnergy();
    g2.calcVerticalSeamInfo();
    t.checkInexact(g2.findMaxSeamInfoWeight(), 36.477, 0.01);
  }

  void testRemoveVerticalSeam(Tester t) {
    initData();
    t.checkExpect(g3, g3);
    g3.calcVerticalSeamInfo();
    g3.removeVerticalSeam();
    t.checkExpect(this.imageEqual(g1_removeGrid.renderImage(), g3.renderImage()), true);
    g3.calcVerticalSeamInfo();
    g3.removeVerticalSeam();
    t.checkExpect(this.imageEqual(g2_removeGrid.renderImage(), g3.renderImage()), true);
    g3.calcVerticalSeamInfo();
    g3.removeVerticalSeam();
    t.checkExpect(this.imageEqual(g3_removeGrid.renderImage(), g3.renderImage()), true);

    t.checkExpect(wideSeamGrid, wideSeamGrid);
    wideSeamGrid.calcVerticalSeamInfo();
    wideSeamGrid.removeVerticalSeam();
    wideSeamGrid.renderImage().saveImage("wide_seam_1.jpg");
    t.checkExpect(this.imageEqual(wideSeamGrid.renderImage(), wideSeam1Grid.renderImage()), true);

  }

  void testRemoveHorizontalSeamSeam(Tester t) {
    initData();
    t.checkExpect(g3, g3);
    g3.calcHorizontalSeamInfo();
    g3.removeHorizontalSeam();
    t.checkExpect(this.imageEqual(g3_removeGrid_hor1.renderImage(), g3.renderImage()), true);
    g3.calcHorizontalSeamInfo();
    g3.removeHorizontalSeam();
    t.checkExpect(this.imageEqual(g3_removeGrid_hor2.renderImage(), g3.renderImage()), true);
    g3.calcHorizontalSeamInfo();
    g3.removeHorizontalSeam();
    t.checkExpect(this.imageEqual(g3_removeGrid_hor3.renderImage(), g3.renderImage()), true);
  }

  void testColorVerticalSeam(Tester t) {
    initData();
    t.checkInexact(g3.getPixel(0, 2).getEnergy(), 0.0, .01);
    t.checkInexact(g3.getPixel(0, 1).getEnergy(), 0.0, .01);
    t.checkExpect(g3.getPixel(1, 2).isRed, false);
    g3.colorVerticalSeam();
    t.checkInexact(g3.getPixel(0, 2).getEnergy(), 1.68, .01);
    t.checkInexact(g3.getPixel(0, 1).getEnergy(), 1.748, .01);
    t.checkExpect(g3.getPixel(1, 2).isRed, true);

  }

  void testColorHorizontalSeam(Tester t) {
    initData();
    t.checkInexact(g3.getPixel(0, 2).getEnergy(), 0.0, .01);
    t.checkInexact(g3.getPixel(0, 1).getEnergy(), 0.0, .01);
    t.checkExpect(g3.getPixel(1, 2).isRed, false);
    g3.colorHorizontalSeam();
    t.checkInexact(g3.getPixel(0, 2).getEnergy(), 1.68, .01);
    t.checkInexact(g3.getPixel(0, 1).getEnergy(), 1.748, .01);
    t.checkExpect(g3.getPixel(1, 2).isRed, true);

  }

  void testVerticalSeamInfo(Tester t) {
    initData();
    t.checkInexact(g1.getPixel(0, 0).getTotalWeight(), 0.0, .01);
    t.checkInexact(g1.getPixel(2, 0).getTotalWeight(), 0.0, .01);
    // must call calcEnergy for energies and calcVerticalSeamInfo for the updated
    // totalWeight
    g1.calcEnergy();
    g1.calcVerticalSeamInfo();
    t.checkInexact(g1.getPixel(2, 1).getTotalWeight(), 2.466, .01);
    t.checkInexact(g1.getPixel(2, 0).getTotalWeight(), 1.662, .01);
  }

  void testHorizontalSeamInfo(Tester t) {
    initData();
    t.checkInexact(g1.getPixel(0, 0).getTotalWeight(), 0.0, .01);
    t.checkInexact(g1.getPixel(2, 0).getTotalWeight(), 0.0, .01);
    // must call calcEnergy for energies and calcVerticalSeamInfo for the updated
    // totalWeight
    g1.calcEnergy();
    g1.calcHorizontalSeamInfo();
    t.checkInexact(g1.getPixel(2, 1).getTotalWeight(), 3.003, .01);
    t.checkInexact(g1.getPixel(2, 0).getTotalWeight(), 3.434, .01);
  }

  void testGetLast(Tester t) {
    initData();
    g1.calcEnergy();
    g1.calcVerticalSeamInfo();
    t.checkExpect(g1.getPixel(0, 0).seamInfo.getLast(), g1.getPixel(0, 0));
    t.checkExpect(g1.getPixel(0, 2).seamInfo.getLast(), g1.getPixel(1, 0));

    g3.calcEnergy();
    g3.calcHorizontalSeamInfo();
    t.checkExpect(g3.getPixel(0, 1).seamInfo.getLast(), g3.getPixel(0, 1));
    t.checkExpect(g3.getPixel(3, 3).seamInfo.getLast(), g3.getPixel(0, 2));
  }

  void testRemoveVerticalSeamSeam(Tester t) {
    initData();
    // testing if a seam is removed
    // setup needed before removing a seam
    g1.colorVerticalSeam();
    t.checkExpect(g1.width, 3);
    Pixel pos10 = g1.getPixel(1, 0);
    Pixel pos00 = g1.getPixel(0, 0);
    t.checkExpect(g1.getPixel(1, 0) == pos10, true);
    t.checkExpect(g1.getPixel(0, 0) == pos00, true);
    g1.removeVerticalSeam();
    t.checkExpect(g1.width, 2);
    // removes this pixel in the image
    // checking if the pixel is gone because the seam would be removed
    t.checkExpect(g1.getPixel(1, 0) == pos10, false);
    // doesnt remove this pixel in the image
    t.checkExpect(g1.getPixel(0, 0) == pos00, true);
  }

  void testRemoveHorizontalSeam(Tester t) {
    initData();
    // testing if a seam is removed
    // setup needed before removing a seam
    g3.colorHorizontalSeam();
    t.checkExpect(g3.height, 4);
    Pixel pos12 = g3.getPixel(1, 2);
    Pixel pos10 = g3.getPixel(0, 2);
    t.checkExpect(g3.getPixel(1, 2) == pos12, true);
    t.checkExpect(g3.getPixel(0, 2) == pos10, true);
    g3.removeHorizontalSeam();
    t.checkExpect(g3.height, 3);
    // removes this pixel in the image
    // checking if the pixel is gone because the seam would be removed
    t.checkExpect(g3.getPixel(1, 2) == pos12, false);
    // doesn't remove this pixel i the image
    t.checkExpect(g3.getPixel(0, 2) == pos10, false);
  }

  void testColorSeam(Tester t) {
    initData();
    g3.calcVerticalSeamInfo();
    g3.getPixel(0, 3).seamInfo.colorSeam();
    t.checkExpect(g3.getPixel(0, 3).isRed, true);
    t.checkExpect(g3.getPixel(0, 2).isRed, true);
    g1.calcVerticalSeamInfo();
    g1.getPixel(0, 2).seamInfo.colorSeam();
    t.checkExpect(g1.getPixel(0, 2).isRed, true);
  }

  void testMakeScene(Tester t) {
    initData();
    SeamCarving scene = new SeamCarving("src/resources/4x4ran.jpg");
    scene.renderType = 1;
    WorldScene image = scene.makeScene();
    WorldScene image2;
    image2 = new WorldScene(4, 4);
    image2.placeImageXY(g3.renderImage(), g3.width / 2, g3.height / 2);
    t.checkExpect(image.equals(image2), true);

    scene.renderType = 2;
    WorldScene imageEng = scene.makeScene();
    WorldScene imageEng2;
    imageEng2 = new WorldScene(4, 4);
    imageEng2.placeImageXY(g3.renderEnergy(), g3.width / 2, g3.height / 2);
    t.checkExpect(imageEng.equals(imageEng2), true);

    scene.renderType = 3;
    WorldScene imageInfoMax = scene.makeScene();
    WorldScene imageInfoMax2;
    imageInfoMax2 = new WorldScene(4, 4);
    imageInfoMax2.placeImageXY(g3.renderEnergy(), g3.width / 2, g3.height / 2);
    t.checkExpect(imageInfoMax.equals(imageInfoMax2), true);
  }

  // test PixelGrid.reinsert() and SeamInfo.reinsert()
  void testReinsert(Tester t) {
    PixelGrid testGrid = new PixelGrid(crossImage);
    PixelGrid exampleGrid = new PixelGrid(crossImage);

    // randomly check removing and reinserting
    // a various numbers of vertical/horizontal seams
    // this ensures that removal and reinsertion of seams functions properly
    // this tests that removing and reinserting 1 to 5 (the size of the image),
    // so we get a wide variety of possible tests, including those where the
    // top left sentinel node is removed and reinserted and every pixel is removed
    // we also randomly select vertical/horizontal
    for (int num = 0; num < 20; num++) {
      testGrid = new PixelGrid(crossImage);
      exampleGrid = new PixelGrid(crossImage);

      for (int ct = 0; ct < 5; ct++) {
        for (int i = 0; i < ct; i++) {
          if (Math.random() < 0.5) {
            testGrid.colorVerticalSeam();
            testGrid.removeVerticalSeam();
          }
          else {
            testGrid.colorHorizontalSeam();
            testGrid.removeHorizontalSeam();
          }
        }

        // reinsert each seam
        for (int i = 0; i < 5; i++) {
          testGrid.reinsertSeam();
        }

        t.checkExpect(this.imageEqual(testGrid.renderImage(), exampleGrid.renderImage()), true);
      }
    }
  }

  void testOnTick(Tester t) {
    SeamCarving test = new SeamCarving("src/resources/balloons.jpg");
    SeamCarving example = new SeamCarving("src/resources/balloons.jpg");

    test.onTick();
    t.checkExpect(test.makeScene(), example.makeScene());

    test.running = true;

    for (int i = 0; i < 5; i++) {
      test.onTick();

      if (test.vertical) {
        example.grid.colorVerticalSeam();
      }
      else {
        example.grid.colorHorizontalSeam();
      }

      t.checkExpect(test.makeScene(), example.makeScene());
      t.checkExpect(test.remove, true);

      test.onTick();

      if (test.vertical) {
        example.grid.removeVerticalSeam();
      }
      else {
        example.grid.removeHorizontalSeam();
      }

      t.checkExpect(test.makeScene(), example.makeScene());
      t.checkExpect(test.remove, false);
    }
  }
   void testGame(Tester t) {
     SeamCarving g = new SeamCarving("src/resources/balloons.jpg");
     g.bigBang(g.grid.width, g.grid.height, 0.0001);
   }
}