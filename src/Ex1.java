/**
 * Introduction to Computer Science 2026, Ariel University,
 * Ex1: arrays, static functions and JUnit
 * https://docs.google.com/document/d/1GcNQht9rsVVSt153Y8pFPqXJVju56CY4/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 *
 * This class represents a set of static methods on a polynomial functions - represented as an array of doubles.
 * The array {0.1, 0, -3, 0.2} represents the following polynomial function: 0.2x^3-3x^2+0.1
 * This is the main Class you should implement (see "add your code below")
 *
 * @author boaz.benmoshe
 */
import java.util.Arrays;

public class Ex1 {
    /**
     * Epsilon value for numerical computation, it serves as a "close enough" threshold.
     */
    public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
    /**
     * The zero polynomial function is represented as an array with a single (0) entry.
     */
    public static final double[] ZERO = {0};

    /**
     * Computes the f(x) value of the polynomial function at x.
     *
     * @param poly - polynomial function
     * @param x
     * @return f(x) - the polynomial function value at x.
     */
    public static double f(double[] poly, double x) {
        double ans = 0;
        for (int i = 0; i < poly.length; i++) {
            double c = Math.pow(x, i);
            ans += c * poly[i];
        }
        return ans;
    }

    /**
     * Given a polynomial function (p), a range [x1,x2] and an epsilon eps.
     * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps,
     * assuming p(x1)*p(x2) <= 0.
     * This function should be implemented recursively.
     *
     * @param p   - the polynomial function
     * @param x1  - minimal value of the range
     * @param x2  - maximal value of the range
     * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
     * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
     */
    public static double root_rec(double[] p, double x1, double x2, double eps) {
        double f1 = f(p, x1);
        double x12 = (x1 + x2) / 2;
        double f12 = f(p, x12);
        if (Math.abs(f12) < eps) {
            return x12;
        }
        if (f12 * f1 <= 0) {
            return root_rec(p, x1, x12, eps);
        } else {
            return root_rec(p, x12, x2, eps);
        }
    }

    /**
     * This function computes a polynomial representation from a set of 2D points on the polynom.
     * The solution is based on: //	http://stackoverflow.com/questions/717762/how-to-calculate-the-vertex-of-a-parabola-given-three-points
     * Note: this function only works for a set of points containing up to 3 points, else returns null.
     *
     * @param xx
     * @param yy
     * @return an array of doubles representing the coefficients of the polynom.
     */
    public static double[] PolynomFromPoints(double[] xx, double[] yy) {
        if (xx == null || yy == null || xx.length != yy.length) return null;

        if (xx.length == 2) {
            if (xx[1] - xx[0] == 0) return null;
            double b = (yy[1] - yy[0]) / (xx[1] - xx[0]);
            double c = yy[0] - b * xx[0];
            return new double[]{c, b};
        }

        if (xx.length == 3) {
            double x1 = xx[0], x2 = xx[1], x3 = xx[2];
            double y1 = yy[0], y2 = yy[1], y3 = yy[2];

            double denom = (x1 - x2) * (x1 - x3) * (x2 - x3);
            if (denom == 0) return null;

            double a = (x3 * (y2 - y1) + x2 * (y1 - y3) + x1 * (y3 - y2)) / denom;
            double b = (x3 * x3 * (y1 - y2) + x2 * x2 * (y3 - y1) + x1 * x1 * (y2 - y3)) / denom;
            double c = (x2 * x3 * (x2 - x3) * y1 + x3 * x1 * (x3 - x1) * y2 + x1 * x2 * (x1 - x2) * y3) / denom;

            return new double[]{c, b, a};
        }

        return null;
    }

    /**
     * Two polynomials functions are equal if and only if they have the same values f(x) for n+1 values of x,
     * where n is the max degree (over p1, p2) - up to an epsilon (aka EPS) value.
     *
     * @param p1 first polynomial function
     * @param p2 second polynomial function
     * @return true iff p1 represents the same polynomial function as p2.
     */
    public static boolean equals(double[] p1, double[] p2) {
        if (p1 == p2) return true;
        if (p1 == null || p2 == null) return false;

        int last1 = p1.length - 1;
        while (last1 >= 0 && Math.abs(p1[last1]) <= EPS) last1--;

        int last2 = p2.length - 1;
        while (last2 >= 0 && Math.abs(p2[last2]) <= EPS) last2--;

        if (last1 != last2) return false;

        for (int i = 0; i <= last1; i++) {
            if (Math.abs(p1[i] - p2[i]) > EPS) return false;
        }

        return true;
    }

    /**
     * Computes a String representing the polynomial function.
     * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
     *
     * @param poly the polynomial function represented as an array of doubles
     * @return String representing the polynomial function:
     */
    public static String poly(double[] poly) {
        String ans = "";

        if (poly.length == 0) {
            ans = "0";
        } else {
            for (int i = poly.length - 1; i >= 0; i--) {
                double c = poly[i];

                if (c != 0) {
                    if (!ans.equals("") && c > 0) ans += " +";
                    if (c < 0) ans += " ";

                    if (i == 0) ans += c;
                    else if (i == 1) ans += c + "x";
                    else ans += c + "x^" + i;
                }
            }
        }

        if (ans.equals("")) ans = "0";
        return ans;
    }

    /**
     * Given two polynomial functions (p1,p2), a range [x1,x2] and an epsilon eps. This function computes an x value (x1<=x<=x2)
     * for which |p1(x) -p2(x)| < eps, assuming (p1(x1)-p2(x1)) * (p1(x2)-p2(x2)) <= 0.
     *
     * @param p1  - first polynomial function
     * @param p2  - second polynomial function
     * @param x1  - minimal value of the range
     * @param x2  - maximal value of the range
     * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
     * @return an x value (x1<=x<=x2) for which |p1(x) - p2(x)| < eps.
     */
    public static double sameValue(double[] p1, double[] p2, double x1, double x2, double eps) {
        double f1 = f(p1, x1) - f(p2, x1);
        double f2 = f(p1, x2) - f(p2, x2);

        if (f1 * f2 > 0) return -1;

        while (Math.abs(x2 - x1) > eps) {
            double mid = (x1 + x2) / 2;
            double fm = f(p1, mid) - f(p2, mid);

            if (Math.abs(fm) < eps) return mid;

            if (fm * f1 <= 0) {
                x2 = mid;
                f2 = fm;
            } else {
                x1 = mid;
                f1 = fm;
            }
        }
        return (x1 + x2) / 2;
    }

    /**
     * Given a polynomial function (p), a range [x1,x2] and an integer with the number (n) of sample points.
     * This function computes an approximation of the length of the function between f(x1) and f(x2)
     * using n inner sample points and computing the segment-path between them.
     * assuming x1 < x2.
     * This function should be implemented iteratively (none recursive).
     *
     * @param p                - the polynomial function
     * @param x1               - minimal value of the range
     * @param x2               - maximal value of the range
     * @param numberOfSegments - (A positive integer value (1,2,...).
     * @return the length approximation of the function between f(x1) and f(x2).
     */
    public static double length(double[] p, double x1, double x2, int numberOfSegments) {
        double dx = (x2 - x1) / numberOfSegments, len = 0, x = x1, y = f(p, x);
        for (int i = 0; i < numberOfSegments; i++) {
            double x2p = x + dx, y2p = f(p, x2p);
            len += Math.sqrt((x2p - x) * (x2p - x) + (y2p - y) * (y2p - y));
            x = x2p;
            y = y2p;
        }
        return len;
    }


    /**
     * Given two polynomial functions (p1,p2), a range [x1,x2] and an integer representing the number of Trapezoids between the functions (number of samples in on each polynom).
     * This function computes an approximation of the area between the polynomial functions within the x-range.
     * The area is computed using Riemann's like integral (https://en.wikipedia.org/wiki/Riemann_integral)
     *
     * @param p1                - first polynomial function
     * @param p2                - second polynomial function
     * @param x1                - minimal value of the range
     * @param x2                - maximal value of the range
     * @param numberOfTrapezoid - a natural number representing the number of Trapezoids between x1 and x2.
     * @return the approximated area between the two polynomial functions within the [x1,x2] range.
     */
    public static double area(double[] p1, double[] p2, double x1, double x2, int numberOfTrapezoid) {
        if (p1 == null || p2 == null || numberOfTrapezoid <= 0) return 0.0;
        if (x1 == x2) return 0.0;

        if (x2 < x1) {
            double t = x1;
            x1 = x2;
            x2 = t;
        }

        double dx = (x2 - x1) / numberOfTrapezoid;
        double sum = 0.0;

        double x = x1;
        double yDiff = Math.abs(f(p1, x) - f(p2, x));

        for (int i = 0; i < numberOfTrapezoid; i++) {
            double xNext = x + dx;
            double yDiffNext = Math.abs(f(p1, xNext) - f(p2, xNext));

            // נוסחת טרפז: (גובה1 + גובה2) * בסיס / 2
            sum += (yDiff + yDiffNext) * dx / 2.0;

            x = xNext;
            yDiff = yDiffNext;
        }

        return sum;
    }

    /**
     * This function computes the array representation of a polynomial function from a String
     * representation. Note:given a polynomial function represented as a double array,
     * getPolynomFromString(poly(p)) should return an array equals to p.
     *
     * @param p - a String representing polynomial function.
     * @return
     */
    public static double[] getPolynomFromString(String p) {
        p = p.replace(" ", "").replace("-", "+-");
        if (p.startsWith("+")) p = p.substring(1);
        String[] t = p.split("\\+");

        int max = 0;
        for (String s : t)
            if (s.contains("^"))
                max = Integer.parseInt(s.substring(s.indexOf("^") + 1));
            else if (s.contains("x"))
                max = Math.max(max, 1);

        double[] ans = new double[max + 1];

        for (String s : t) {
            if (s.equals("")) {
            } else if (s.contains("x")) {
                int xPos = s.indexOf("x");
                String c = s.substring(0, xPos);
                double coef = c.equals("") ? 1 : (c.equals("-") ? -1 : Double.parseDouble(c));
                int pow = s.contains("^") ? Integer.parseInt(s.substring(s.indexOf("^") + 1)) : 1;
                ans[pow] += coef;
            } else {
                ans[0] += Double.parseDouble(s);
            }
        }
        return ans;
    }

    /**
     * This function computes the polynomial function which is the sum of two polynomial functions (p1,p2)
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] add(double[] p1, double[] p2) {
        int len1 = p1.length;
        int len2 = p2.length;
        int maxLen = Math.max(len1, len2);
        double[] ans = new double[maxLen];
        double j, m;

        for (int i = 0; i < maxLen; i++) {
            if (i < p1.length) {
                j = p1[i];
            } else {
                j = 0;
            }
            if (i < p2.length) {
                m = p2[i];
            } else {
                m = 0;
            }
            ans[i] = j + m;
        }

        return ans;
    }

    /**
     * This function computes the polynomial function which is the multiplication of two polynoms (p1,p2)
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] mul(double[] p1, double[] p2) {

        int len1 = p1.length;
        int len2 = p2.length;
        double[] ans = new double[p1.length + p2.length - 1];

        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p2.length; j++) {

                ans[i + j] += p1[i] * p2[j];
            }
        }

        return ans;
    }

    /**
     * This function computes the derivative of the p0 polynomial function.
     *
     * @param po
     * @return
     */
    public static double[] derivative(double[] po) {
        if (po == null || po.length <= 1) {
            return new double[]{0.0};
        }
        double[] derivate = new double[po.length - 1];
        for (int i = 1; i < po.length; i++) {
            derivate[i - 1] = i * po[i];
        }
        return derivate;
    }

    public static double[] mp(double current, int pad, double[] p) {
        double[] m = new double[p.length + pad];
        for (int j = 0; j < p.length; j++) {
            m[j + pad] = current * p[j];
        }
        return m;
    }
}