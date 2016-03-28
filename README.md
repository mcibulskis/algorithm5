# algorithm5
An implementation of the Algorithm5 fuzzy clustering algorithm in Scala.

**Note: This implementation is not yet finished or ready for use.**

## Examples of Previous Uses of Algorithm5
The original implementation was in C and was used for chemical clustering to assist in the intelligent generation of assay plates.  This relied upon the Daylight libraries for the fingerprint (feature set) generation.  The clustering and distance calculations were performed on Silicon Graphics workstations.

A subsequent implementation was also created in C and was used for the detection of undesired student collaboration on specific programming assignments (with class sizes of upwards of 400 students).  The "distance" was based upon multiple calculations of longest common subsets of lines of canonicalized C code (using lex and yacc).  The clustering and distance calculations were performed on Sun workstations.

## References
#### Original Paper
> Doman, T. N., Cibulskis, J. M., Cibulskis, M. J., McCray, P. D., & Spangler, D. P. (1996). Algorithm5: A technique for fuzzy similarity clustering of chemical inventories. Journal of chemical information and computer sciences, 36(6), 1195-1204.

PDF Available from: [http://pubs.acs.org/doi/abs/10.1021/ci960361r](http://pubs.acs.org/doi/abs/10.1021/ci960361r)

#### Daylight User Group Meeting Presentation Slides
http://www.daylight.com/meetings/mug96/doman/Doman.Overheads.html
