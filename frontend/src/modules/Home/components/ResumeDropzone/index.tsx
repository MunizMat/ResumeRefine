/* --------------- External ------------- */
import { Flex, Group, Image, Loader, Text, rem } from '@mantine/core';
import { IconUpload, IconPhoto, IconX } from '@tabler/icons-react';
import { Dropzone, FileWithPath, MIME_TYPES } from '@mantine/dropzone';
import { useState } from 'react';

/* --------------- Services ------------- */
import { resume as resumeService } from '../../../../services/resume';
import { useResume } from '../../../../contexts/Resume';

export const ResumeDropzone = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const { setResume, resume } = useResume();

  const handleDrop = async (files: FileWithPath[]) => {
    const newFile = files[0];

    if (!newFile) return;

    try {
      setLoading(true);

      const { filename, resumeId, url } = await resumeService.saveResume({
        resume: newFile,
      });

      setResume({ filename, resumeId, url });
    } catch (error) {
      console.error(error);
    }

    setLoading(false);
  };

  return (
    <Dropzone
      onDrop={handleDrop}
      onReject={(files) => console.log('rejected files', files)}
      maxSize={5 * 1024 ** 2}
      multiple={false}
      accept={[MIME_TYPES.pdf]}
      bg="transparent"
    >
      <Group
        justify="center"
        gap="xl"
        mih={220}
        style={{ pointerEvents: 'none' }}
      >
        <Dropzone.Accept>
          <IconUpload
            style={{
              width: rem(52),
              height: rem(52),
              color: 'var(--mantine-color-blue-6)',
            }}
            stroke={1.5}
          />
        </Dropzone.Accept>
        <Dropzone.Reject>
          <IconX
            style={{
              width: rem(52),
              height: rem(52),
              color: 'var(--mantine-color-red-6)',
            }}
            stroke={1.5}
          />
        </Dropzone.Reject>
        <Dropzone.Idle>
          {!loading && !resume && (
            <IconPhoto
              style={{
                width: rem(52),
                height: rem(52),
                color: 'var(--mantine-color-dimmed)',
              }}
              stroke={1.5}
            />
          )}
        </Dropzone.Idle>

        {(() => {
          switch (true) {
            case loading:
              return <Loader />;
            case Boolean(resume):
              return (
                <Flex
                  direction="column"
                  align="center"
                  justify="center"
                  gap={8}
                >
                  <Text>{resume?.filename}</Text>
                  <Image
                    src={resume?.url}
                    radius={8}
                    width={200}
                    height={300}
                  />
                </Flex>
              );
            default:
              return (
                <div>
                  <Text size="xl" inline>
                    Drag images here or click to select files
                  </Text>
                  <Text size="sm" c="dimmed" inline mt={7}>
                    Your file should not exceed 5mb
                  </Text>
                </div>
              );
          }
        })()}
      </Group>
    </Dropzone>
  );
};
