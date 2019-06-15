int approxEqual(float el, float reference, float tolerance)
{
  return (reference-tolerance <= el &&
	  el <= reference+tolerance);
}
