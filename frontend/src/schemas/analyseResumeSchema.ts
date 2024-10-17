import z from 'zod';

export const analyseResumeSchema = z.object({
  email: z.string().email(),

  // File object
  resume: z.custom<File>((val) => Boolean(val)).nullable(),
});
